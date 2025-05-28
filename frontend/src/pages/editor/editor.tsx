import NavBar from "../../components/navbar/navbar.tsx";
import {Alert, Box, Button, CircularProgress, Divider, Snackbar, Typography} from "@mui/material";
import uploadUrl from "../../assets/UploadIcon.svg";
import downloadUrl from "../../assets/DownloadIcon.svg";
import BackgroundBox from "../../components/backgroundbox/backgroundbox.tsx";
import React, {useEffect, useRef, useState} from "react";
import {Editor} from "@monaco-editor/react";
import ScaledIframe from "../../components/scaled-iframe/scaled-iframe.tsx";
import {useNavigate, useParams} from "react-router-dom";
import {Prez} from "../../data/models/Prez.tsx";
import {uploadPptx} from "../../apis/pptxParserApi.tsx";
import {fetchPrezById, updatePrez} from "../../apis/prezApi.tsx";

export const EditorPage: React.FC = () => {
    const navigator = useNavigate();

    const {id} = useParams<{ id: string }>();

    const [prez, setPrez] = useState<Prez | null>(null);
    const [htmlCode, setHtmlCode] = useState<string>("");
    const [convertedHtmlCode, setConvertedHtmlCode] = useState<string>("");
    const [title, setTitle] = useState<string>("");
    const [error, setError] = useState<string | null>(null);
    const [openSnackbar, setOpenSnackbar] = useState(false);
    const [isLoading, setIsLoading] = useState(false);
    const [isFullscreen, setIsFullscreen] = useState(false);
    const [currentSlide, setCurrentSlide] = useState(0);
    const fileInputRef = useRef<HTMLInputElement>(null);
    const fullscreenRef = useRef<HTMLDivElement>(null);

    const getSlides = () => {
        if (!convertedHtmlCode) return [];
        const parser = new DOMParser();
        const doc = parser.parseFromString(convertedHtmlCode, 'text/html');
        return Array.from(doc.querySelectorAll('div[id^="page"]'));
    };

    const slides = getSlides();

    useEffect(() => {
        if (!isFullscreen) return;

        const handleKeyDown = (e: KeyboardEvent) => {
            if (e.key === 'Escape') {
                handleExitFullscreen();
            } else if (e.key === 'ArrowRight' || e.key === 'ArrowDown') {
                handleNextSlide();
            } else if (e.key === 'ArrowLeft' || e.key === 'ArrowUp') {
                handlePrevSlide();
            }
        };

        window.addEventListener('keydown', handleKeyDown);
        return () => window.removeEventListener('keydown', handleKeyDown);
    }, [isFullscreen, currentSlide, slides.length]);


    const handleNavigateToHome = async () => {
        if (!prez) {
            navigator('/');
            return;
        }

        try {
            await updatePrez(
                prez.id,
                prez.ownerId,
                title,
                htmlCode
            );
            navigator('/');
        } catch (error) {
            console.error("Ошибка при сохранении перед переходом:", error);
            setError("Не удалось сохранить изменения перед переходом");
            setOpenSnackbar(true);
        }
    };

    const handleNextSlide = () => {
        setCurrentSlide(prev => Math.min(prev + 1, slides.length - 1));
    };

    const handlePrevSlide = () => {
        setCurrentSlide(prev => Math.max(prev - 1, 0));
    };

    const handleStartPresentation = () => {
        setIsFullscreen(true);
        setCurrentSlide(0);
        document.documentElement.requestFullscreen().catch(e => {
            console.error('Error attempting to enable fullscreen:', e);
        });
    };

    const handleExitFullscreen = () => {
        setIsFullscreen(false);
        if (document.fullscreenElement) {
            document.exitFullscreen().catch(e => {
                console.error('Error attempting to exit fullscreen:', e);
            });
        }
    };

    const handleDownload = () => {
        const blob = new Blob([htmlCode], {type: 'text/html'});
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `${title}.html`;
        a.click();
        URL.revokeObjectURL(url);
    };

    const handleUploadClick = () => {
        fileInputRef.current?.click();
    };

    const handleChangeTitle = async (newTitle: string) => {
        setTitle(newTitle);

        if (prez) {
            try {
                const updatedPrez = await updatePrez(
                    prez.id,
                    prez.ownerId,
                    newTitle,
                    htmlCode
                );
                setPrez(updatedPrez);
                setConvertedHtmlCode(updatedPrez.convertedHtml);
            } catch (error) {
                console.error("Ошибка при обновлении заголовка:", error);
                setError("Не удалось обновить заголовок");
                setOpenSnackbar(true);
            }
        }
    };

    const handlePreview = async () => {
        if (!prez) return;

        try {
            const updatedPrez = await updatePrez(
                prez.id,
                prez.ownerId,
                title,
                htmlCode
            );
            setPrez(updatedPrez);
            setConvertedHtmlCode(updatedPrez.convertedHtml);
        } catch (error) {
            console.error("Ошибка при обновлении предпросмотра:", error);
            setError("Не удалось обновить предпросмотр");
            setOpenSnackbar(true);
        }
    };

    const handleFileChange = async (e: React.ChangeEvent<HTMLInputElement>) => {
        const file = e.target.files?.[0];
        if (!file) return;

        setIsLoading(true);
        setError(null);

        try {
            const response = await uploadPptx(file);
            setHtmlCode(response);

            const updatedPrez = {
                ...prez!,
                title: title,
                customHtml: response,
                convertedHtml: response
            };

            const serverResponse = await updatePrez(
                updatedPrez.id,
                updatedPrez.ownerId,
                updatedPrez.title,
                updatedPrez.customHtml
            );

            setPrez(serverResponse);
            setConvertedHtmlCode(serverResponse.convertedHtml);
        } catch (error) {
            let errorMessage = 'Произошла ошибка при загрузке файла';
            if (error instanceof Error) {
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

    const loadPrez = async () => {
        setIsLoading(true);
        try {
            const prezResponse = await fetchPrezById(id!);
            setPrez(prezResponse);
            setHtmlCode(prezResponse.customHtml);
            setConvertedHtmlCode(prezResponse.convertedHtml);
            setTitle(prezResponse.title);
        } catch {
            setError("Ошибка при загрузке продукта");
        } finally {
            setIsLoading(false);
        }
    };

    useEffect(() => {
        if (id) {
            loadPrez();
        }
    }, [id]);

    if (!prez) {
        return (
            <BackgroundBox>
                <NavBar needSearchLine={false} needButtonToSlides={true} onNavigateToHome={handleNavigateToHome}></NavBar>
                <Box sx={{
                    display: "flex",
                    justifyContent: "center",
                    alignItems: "center",
                    marginY: "20%",
                    marginX: "25%",
                    width: "800px",
                    minHeight: "100px",
                    backgroundColor: "#FFFFFF",
                    border: "1px solid rgba(27, 44, 44, 1)",
                    borderRadius: "5px"
                }}>
                    <Typography
                        sx={{display: "flex", justifyContent: "center", alignItems: "center", fontSize: "2rem"}}>
                        Презентации с данным id не существует
                    </Typography>
                </Box>
            </BackgroundBox>
        )
    }

    // Fullscreen presentation view
    if (isFullscreen) {
        return (
            <div
                ref={fullscreenRef}
                style={{
                    position: 'fixed',
                    top: 0,
                    left: 0,
                    width: '100%',
                    height: '100%',
                    backgroundColor: 'white',
                    display: 'flex',
                    justifyContent: 'center',
                    alignItems: 'center',
                    zIndex: 9999
                }}
                onClick={() => handleNextSlide()}
            >
                {slides.length > 0 && (
                    <div
                        style={{
                            width: '100%',
                            height: '100%',
                            display: 'flex',
                            justifyContent: 'center',
                            alignItems: 'center'
                        }}
                    >
                        {/* Используем iframe для безопасного отображения HTML */}
                        <iframe
                            srcDoc={slides[currentSlide].outerHTML}
                            style={{
                                width: '100%',
                                height: '100%',
                                border: 'none',
                                alignItems: "center",
                                justifyContent: "center",
                                marginLeft: "40px",
                                marginTop: "30px"
                            }}
                            sandbox="allow-top-navigation-by-user-activation allow-top-navigation allow-scripts allow-same-origin allow-popups" // Ограничиваем возможности iframe для безопасности
                        />
                    </div>
                )}
                <div style={{
                    position: 'absolute',
                    bottom: '20px',
                    right: '20px',
                    backgroundColor: 'rgba(0,0,0,0.5)',
                    color: 'white',
                    padding: '10px',
                    borderRadius: '5px'
                }}>
                    {currentSlide + 1} / {slides.length}
                </div>
                <button
                    onClick={(e) => {
                        e.stopPropagation();
                        handleExitFullscreen();
                    }}
                    style={{
                        position: 'absolute',
                        top: '20px',
                        right: '20px',
                        backgroundColor: 'rgba(0,0,0,0.5)',
                        color: 'white',
                        border: 'none',
                        padding: '10px',
                        borderRadius: '5px',
                        cursor: 'pointer'
                    }}
                >
                    Выход (Esc)
                </button>
            </div>
        );
    }

    return (
        <BackgroundBox>
            <NavBar needSearchLine={false} needButtonToSlides={true} slidesTitle={title}
                    onTitleChange={handleChangeTitle} onNavigateToHome={handleNavigateToHome}/>

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
                        }}
                                onClick={handlePreview}>
                            <Typography sx={{textTransform: "none", fontSize: "0.9rem"}}>
                                Предпросмотр
                            </Typography>
                        </Button>
                        <Button
                            variant="contained"
                            sx={{
                                backgroundColor: "#098842",
                                borderRadius: "45px",
                                boxShadow: "none"
                            }}
                            onClick={handleStartPresentation}
                        >
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
                        <ScaledIframe src={convertedHtmlCode}></ScaledIframe>
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