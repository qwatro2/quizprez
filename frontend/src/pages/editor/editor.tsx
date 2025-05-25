import NavBar from "../../components/navbar/navbar.tsx";
import {Box, Button, Divider, Typography} from "@mui/material";
import uploadUrl from "../../assets/UploadIcon.svg";
import downloadUrl from "../../assets/DownloadIcon.svg";
import BackgroundBox from "../../components/backgroundbox/backgroundbox.tsx";
import React, {useRef, useState} from "react";
import {Editor} from "@monaco-editor/react";

export const EditorPage: React.FC = () => {
    const [htmlCode, setHtmlCode] = useState<string>("<!DOCTYPE html>\n<html>\n<head>\n  <title>Пример</title>\n</head>\n<body>\n  <h1>Привет, мир!</h1>\n</body>\n</html>");
    const iframeRef = useRef<HTMLIFrameElement>(null);

    const handleDownload = () => {
        const blob = new Blob([htmlCode], { type: 'text/html' });
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = 'index.html';
        a.click();
        URL.revokeObjectURL(url);
    };

    return (
        <BackgroundBox>
            <NavBar needSearchLine={false} needButtonToSlides={true} slidesTitle={"БЧХ-коды"} />

            <Box sx={{
                display: "flex",
                flexDirection: "column",
                flex: 1,
                overflow: 'hidden'
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
                        <Box
                            component="img"
                            src={uploadUrl}
                            sx={{width: 25, height: 25, cursor: "pointer"}}
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
                    }}>
                        <Button variant="contained" sx={{
                            backgroundColor: "#098842",
                            borderRadius: "45px",
                            boxShadow: "none"
                        }}>
                            <Typography sx={{textTransform: "none", fontSize: "1rem"}}>
                                Предпросмотр
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
                        overflow: 'hidden'
                    }}>
                        <Editor
                            height="100%"
                            defaultLanguage="html"
                            value={htmlCode}
                            onChange={(value) => setHtmlCode(value || "")}
                            options={{
                                minimap: { enabled: false },
                                fontSize: 14,
                                wordWrap: 'on',
                                scrollBeyondLastLine: false
                            }}
                        />
                    </Box>

                    <Divider orientation="vertical" sx={{
                        width: "6px",
                        backgroundColor: "#2F3A4C",
                        border: "none"
                    }} />

                    {/* Предпросмотр */}
                    <Box sx={{
                        width: "50%",
                        height: "100%",
                        backgroundColor: 'white',
                        overflow: 'hidden'
                    }}>
                        <iframe
                            ref={iframeRef}
                            title="preview"
                            srcDoc={htmlCode}
                            style={{
                                width: '100%',
                                height: '100%',
                                border: 'none'
                            }}
                        />
                    </Box>
                </Box>
            </Box>
        </BackgroundBox>
    );
};

export default EditorPage;