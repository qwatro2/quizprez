import NavBar from "../../components/navbar/navbar.tsx";
import {Box, Button, Divider, Typography, Snackbar, Alert, CircularProgress} from "@mui/material";
import uploadUrl from "../../assets/UploadIcon.svg";
import downloadUrl from "../../assets/DownloadIcon.svg";
import BackgroundBox from "../../components/backgroundbox/backgroundbox.tsx";
import React, {useRef, useState} from "react";
import {Editor} from "@monaco-editor/react";
import axios from 'axios';
import ScaledIframe from "../../components/scaled-iframe.tsx";

export const EditorPage: React.FC = () => {
    const [htmlCode, setHtmlCode] = useState<string>("<!DOCTYPE html>\n<html>\n<head>\n  <title>Пример</title>\n</head>\n<body>\n  <h1>Привет, мир!</h1>\n</body>\n</html>");
    const [error, setError] = useState<string | null>(null);
    const [openSnackbar, setOpenSnackbar] = useState(false);
    const [isLoading, setIsLoading] = useState(false);
    const fileInputRef = useRef<HTMLInputElement>(null);

    const handleDownload = () => {
        const blob = new Blob([htmlCode], {type: 'text/html'});
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = 'index.html';
        a.click();
        URL.revokeObjectURL(url);
    };

    const handleUploadClick = () => {
        fileInputRef.current?.click();
    };

    const handleFileChange = async (e: React.ChangeEvent<HTMLInputElement>) => {
        const file = e.target.files?.[0];
        if (!file) return;

        setIsLoading(true);
        setError(null);

        try {
            const formData = new FormData();
            formData.append('file', file);

            const api = axios.create({
                baseURL: 'http://localhost:8088/api/v1',
                timeout: 300000,
                headers: {
                    'Content-Type': 'multipart/form-data',
                    'accept': '*/*'
                },
            });

            const response = await api.post('/parse/pptx', formData, {
                onUploadProgress: (progressEvent) => {
                    const percentCompleted = Math.round(
                        (progressEvent.loaded * 100) / (progressEvent.total || 1)
                    );
                    console.log(`Upload progress: ${percentCompleted}%`);
                },
            });

            if (response.data) {
                setHtmlCode(response.data);
            }
        } catch (error) {
            let errorMessage = 'Произошла ошибка при загрузке файла';

            if (axios.isAxiosError(error)) {
                if (error.code === 'ECONNABORTED') {
                    errorMessage = 'Превышено время ожидания ответа от сервера. Попробуйте уменьшить размер файла или повторить попытку позже.';
                } else if (error.code === 'ERR_NETWORK') {
                    errorMessage = 'Не удалось подключиться к серверу. Проверьте, запущен ли бекенд и доступен ли по указанному адресу.';
                } else if (error.response) {
                    errorMessage = `Ошибка сервера: ${error.response.status} - ${error.response.data?.message || 'Неизвестная ошибка'}`;
                }
            } else if (error instanceof Error) {
                errorMessage = error.message;
            }

            console.error('Error uploading file:', error);
            setError(errorMessage);
            setOpenSnackbar(true);
        } finally {
            setIsLoading(false);
            if (e.target) {
                e.target.value = '';
            }
        }
    };

    const handleCloseSnackbar = () => {
        setOpenSnackbar(false);
    };

    return (
        <BackgroundBox>
            <NavBar needSearchLine={false} needButtonToSlides={true} slidesTitle={"БЧХ-коды"}/>

            <Box sx={{
                display: "flex",
                flexDirection: "column",
                flex: 1,
                overflow: 'hidden',
            }}>
                {/* Панель инструментов */}
                <Box sx={{
                    height: "50px",
                    backgroundColor: "#2F3A4C",
                    display: "flex",
                    flexDirection: "row",
                    marginTop: "60px"
                }}>
                    <Box sx={{
                        minWidth: "50%",
                        maxWidth: "50%",
                        display: "flex",
                        justifyContent: "center",
                        alignItems: "center",
                        gap: "20px"
                    }}>
                        <input
                            type="file"
                            ref={fileInputRef}
                            onChange={handleFileChange}
                            accept=".pptx"
                            style={{display: 'none'}}
                        />
                        <Box
                            component="img"
                            src={uploadUrl}
                            sx={{width: 25, height: 25, cursor: "pointer"}}
                            onClick={handleUploadClick}
                        />
                        <Box
                            component="img"
                            src={downloadUrl}
                            sx={{width: 25, height: 25, cursor: "pointer"}}
                            onClick={handleDownload}
                        />
                    </Box>

                    <Box sx={{
                        minWidth: "50%",
                        maxWidth: "50%",
                        display: "flex",
                        justifyContent: "center",
                        alignItems: "center",
                        gap: "20px"
                    }}>
                        <Button variant="contained" sx={{
                            backgroundColor: "#098842",
                            borderRadius: "45px",
                            boxShadow: "none"
                        }}>
                            <Typography sx={{textTransform: "none", fontSize: "0.9rem"}}>
                                Предпросмотр
                            </Typography>
                        </Button>
                        <Button variant="contained" sx={{
                            backgroundColor: "#098842",
                            borderRadius: "45px",
                            boxShadow: "none"
                        }}>
                            <Typography sx={{textTransform: "none", fontSize: "0.9rem"}}>
                                Демонстрация
                            </Typography>
                        </Button>
                    </Box>
                </Box>

                {/* Основная область - редактор и предпросмотр */}
                <Box sx={{
                    display: "flex",
                    flexDirection: "row",
                    overflow: 'hidden',
                    height: "100%"
                }}>
                    {/* Редактор кода */}
                    <Box sx={{
                        width: "50%",
                        height: "100%",
                        borderRight: "1px solid #2F3A4C",
                        overflow: 'hidden',
                    }}>
                        {isLoading &&
                            <Box sx={{
                                backgroundColor: "white",
                                display: "flex",
                                alignItems: "center",
                                justifyContent: "center",
                                height: "100%"
                            }}>
                                <CircularProgress size={40} color="success"/>
                            </Box>
                        }
                        {!isLoading &&
                            <Editor
                                height="100%"
                                defaultLanguage="html"
                                value={htmlCode}
                                onChange={(value) => setHtmlCode(value || "")}
                                options={{
                                    minimap: {enabled: false},
                                    fontSize: 14,
                                    wordWrap: 'on',
                                    scrollBeyondLastLine: false
                                }}
                            />
                        }
                    </Box>

                    <Divider orientation="vertical" sx={{
                        width: "6px",
                        backgroundColor: "#2F3A4C",
                        border: "none"
                    }}/>

                    {/* Предпросмотр */}
                    <Box sx={{
                        width: "50%",
                        height: "100%",
                        backgroundColor: 'white',
                        overflow: 'hidden',
                    }}>
                        <ScaledIframe src={htmlCode}></ScaledIframe>
                    </Box>
                </Box>
            </Box>

            {/* Снэкбар для отображения ошибок */}
            <Snackbar
                open={openSnackbar}
                autoHideDuration={6000}
                onClose={handleCloseSnackbar}
                anchorOrigin={{vertical: 'bottom', horizontal: 'center'}}
            >
                <Alert onClose={handleCloseSnackbar} severity="error" sx={{width: '100%'}}>
                    {error}
                </Alert>
            </Snackbar>
        </BackgroundBox>
    );
};

export default EditorPage;