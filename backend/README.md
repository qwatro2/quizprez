# QuizPrez. Backend part
## Запуск
Для запуска бэкенда необходимо:
1. Расположить `.env` следующего содержания:
    ```env
    GATEWAY_BACKEND_PROTOCOL=http
    GATEWAY_BACKEND_HOST=quizprez-gateway
    GATEWAY_BACKEND_PORT=8080

    AUTH_BACKEND_PROTOCOL=http
    AUTH_BACKEND_HOST=quizprez-auth
    AUTH_BACKEND_PORT=8082

    PRESENTATION_BACKEND_PROTOCOL=http
    PRESENTATION_BACKEND_HOST=quizprez-presentation
    PRESENTATION_BACKEND_PORT=8084

    QUIZ_BACKEND_PROTOCOL=http
    QUIZ_BACKEND_HOST=quizprez-quiz
    QUIZ_BACKEND_PORT=8086

    PPTX_BACKEND_PROTOCOL=http
    PPTX_BACKEND_HOST=quizprez-pptx-parsing
    PPTX_BACKEND_PORT=8088

    DB_HOST=postgres
    DB_PORT=5432
    DB_NAME=quizprez-db
    DB_USERNAME=postgresusername
    DB_PASSWORD=123456

    OAUTH_CLIENT_ID=XXXXXX-XXXXXXX.apps.googleusercontent.com
    OAUTH_CLIENT_SECRET=XXXXXXXX-XXXXXX_XXXXXXX

    JWT_SECRET=your-jwt-secret

    MAIL_USERNAME=your-mail-username
    MAIL_PASSWORD=your-mail-password

    FRONTEND_BASEURL=your-frontend-base-url
    QUIZ_BACKEND_BASEURL=your-backend-base-url
    ```
2. Запустить `Docker`, затем 
    ```bash
    cd backend
    docker-compose up --build -d
    ```

## API-спецификация Auth Service
```json
{
  "openapi": "3.1.0",
  "info": {
    "title": "OpenAPI definition",
    "version": "v1"
  },
  "paths": {
    "/api/v1/auth/register": {
      "post": {
        "tags": [
          "auth-controller"
        ],
        "operationId": "register",
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/RegistrationRequest"
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          }
        }
      }
    },
    "/api/v1/auth/refresh": {
      "post": {
        "tags": [
          "auth-controller"
        ],
        "operationId": "refresh",
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/RefreshTokenRequest"
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          }
        }
      }
    },
    "/api/v1/auth/login": {
      "post": {
        "tags": [
          "auth-controller"
        ],
        "operationId": "login",
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/LoginRequest"
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          }
        }
      }
    },
    "/api/v1/auth/confirm": {
      "get": {
        "tags": [
          "auth-controller"
        ],
        "operationId": "confirmEmail",
        "parameters": [
          {
            "name": "token",
            "in": "query",
            "required": true,
            "schema": {
              "type": "string"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          }
        }
      }
    }
  },
  "components": {
    "schemas": {
      "RegistrationRequest": {
        "type": "object",
        "properties": {
          "email": {
            "type": "string",
            "minLength": 1
          },
          "password": {
            "type": "string",
            "maxLength": 2147483647,
            "minLength": 8
          }
        },
        "required": [
          "email"
        ]
      },
      "RefreshTokenRequest": {
        "type": "object",
        "properties": {
          "refreshToken": {
            "type": "string"
          }
        }
      },
      "LoginRequest": {
        "type": "object",
        "properties": {
          "email": {
            "type": "string",
            "minLength": 1
          },
          "password": {
            "type": "string",
            "minLength": 1
          }
        },
        "required": [
          "email",
          "password"
        ]
      }
    }
  }
}
```

## API-спецификация Presentation Service
```json
{
  "openapi": "3.1.0",
  "info": {
    "title": "OpenAPI definition",
    "version": "v1"
  },
  "paths": {
    "/api/v1/presentations/{id}": {
      "get": {
        "tags": [
          "presentation-controller"
        ],
        "operationId": "getById",
        "parameters": [
          {
            "name": "id",
            "in": "path",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int64"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/PresentationResponse"
                }
              }
            }
          }
        }
      },
      "put": {
        "tags": [
          "presentation-controller"
        ],
        "operationId": "update",
        "parameters": [
          {
            "name": "id",
            "in": "path",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int64"
            }
          },
          {
            "name": "buttonMode",
            "in": "query",
            "required": false,
            "schema": {
              "type": "string",
              "enum": [
                "PREVIEW",
                "DEMO",
                "SAVE"
              ]
            }
          }
        ],
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/PresentationRequest"
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/PresentationResponse"
                }
              }
            }
          }
        }
      },
      "delete": {
        "tags": [
          "presentation-controller"
        ],
        "operationId": "delete",
        "parameters": [
          {
            "name": "id",
            "in": "path",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int64"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK"
          }
        }
      }
    },
    "/api/v1/presentations": {
      "get": {
        "tags": [
          "presentation-controller"
        ],
        "operationId": "getAll",
        "parameters": [
          {
            "name": "ownerId",
            "in": "query",
            "required": false,
            "schema": {
              "type": "integer",
              "format": "int64"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "array",
                  "items": {
                    "$ref": "#/components/schemas/PresentationResponse"
                  }
                }
              }
            }
          }
        }
      },
      "post": {
        "tags": [
          "presentation-controller"
        ],
        "operationId": "create",
        "parameters": [
          {
            "name": "buttonMode",
            "in": "query",
            "required": false,
            "schema": {
              "type": "string",
              "enum": [
                "PREVIEW",
                "DEMO",
                "SAVE"
              ]
            }
          }
        ],
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/PresentationRequest"
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/PresentationResponse"
                }
              }
            }
          }
        }
      }
    }
  },
  "components": {
    "schemas": {
      "PresentationRequest": {
        "type": "object",
        "properties": {
          "ownerId": {
            "type": "integer",
            "format": "int64"
          },
          "title": {
            "type": "string"
          },
          "html": {
            "type": "string"
          }
        }
      },
      "PresentationResponse": {
        "type": "object",
        "properties": {
          "id": {
            "type": "integer",
            "format": "int64"
          },
          "ownerId": {
            "type": "integer",
            "format": "int64"
          },
          "title": {
            "type": "string"
          },
          "customHtml": {
            "type": "string"
          },
          "convertedHtml": {
            "type": "string"
          }
        }
      }
    }
  }
}
```

## API-спецификация Quiz Service
```json
{
  "openapi": "3.1.0",
  "info": {
    "title": "OpenAPI definition",
    "version": "v1"
  }
  "paths": {
    "/api/v1/participant/answer": {
      "put": {
        "tags": [
          "participant-controller"
        ],
        "operationId": "answer",
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/ParticipantAnswerRequest"
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          }
        }
      }
    },
    "/api/v1/participant/join": {
      "post": {
        "tags": [
          "participant-controller"
        ],
        "operationId": "join",
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/ParticipantJoinRequest"
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          }
        }
      }
    },
    "/api/v1/admin/create": {
      "post": {
        "tags": [
          "admin-controller"
        ],
        "operationId": "createSession",
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/CreateSessionRequest"
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          }
        }
      }
    },
    "/api/v1/admin/results": {
      "get": {
        "tags": [
          "admin-controller"
        ],
        "operationId": "getAllResults",
        "parameters": [
          {
            "name": "ownerId",
            "in": "query",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int64"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          }
        }
      }
    },
    "/api/v1/admin/results/{code}": {
      "get": {
        "tags": [
          "admin-controller"
        ],
        "operationId": "getResults",
        "parameters": [
          {
            "name": "code",
            "in": "path",
            "required": true,
            "schema": {
              "type": "string"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          }
        }
      }
    }
  },
  "components": {
    "schemas": {
      "ParticipantAnswerRequest": {
        "type": "object",
        "properties": {
          "participantName": {
            "type": "string"
          },
          "sessionCode": {
            "type": "string"
          },
          "quizId": {
            "type": "integer",
            "format": "int64"
          },
          "questionId": {
            "type": "integer",
            "format": "int64"
          },
          "chosenAnswerId": {
            "type": "integer",
            "format": "int64"
          }
        }
      },
      "ParticipantJoinRequest": {
        "type": "object",
        "properties": {
          "sessionCode": {
            "type": "string"
          },
          "name": {
            "type": "string"
          }
        }
      },
      "CreateSessionRequest": {
        "type": "object",
        "properties": {
          "ownerId": {
            "type": "integer",
            "format": "int64"
          },
          "html": {
            "type": "string"
          }
        }
      }
    }
  }
}
```

## API-спецификация Pptx Parsing Service
```json
{
  "openapi": "3.1.0",
  "info": {
    "title": "OpenAPI definition",
    "version": "v1"
  },
  "paths": {
    "/api/v1/parse/pptx": {
      "post": {
        "tags": [
          "parser-controller"
        ],
        "operationId": "parsePptx",
        "requestBody": {
          "content": {
            "multipart/form-data": {
              "schema": {
                "type": "object",
                "properties": {
                  "file": {
                    "type": "string",
                    "format": "binary"
                  }
                },
                "required": [
                  "file"
                ]
              }
            }
          }
        },
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "*/*": {
                "schema": {
                  "type": "object"
                }
              }
            }
          }
        }
      }
    },
    "/api/v1/image/{imagePath}": {
      "get": {
        "tags": [
          "image-controller"
        ],
        "operationId": "image",
        "parameters": [
          {
            "name": "imagePath",
            "in": "path",
            "required": true,
            "schema": {
              "type": "string"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "image/jpeg": {
                "schema": {
                  "type": "string",
                  "format": "binary"
                }
              }
            }
          }
        }
      }
    }
  },
  "components": {

  }
}
```
