import React, {useCallback, useState} from "react";
import {
    Box,
    Button,
    Card,
    CardActions,
    CardContent,
    CardHeader,
    CircularProgress,
    Divider,
    IconButton,
    TextField,
    Typography
} from "@mui/material";
import {Close as CloseIcon, CloudUpload as CloudUploadIcon} from "@mui/icons-material";
import {uploadPptx} from "../../apis/pptxParserApi.tsx";
import {uploadPrez} from "../../apis/prezApi.tsx";
import {useNavigate} from "react-router-dom";

const BASE_HTML_CODE = "<!DOCTYPE html>\n" +
    "<html>\n" +
    "<head>\n" +
    "    <title>Hello, World!</title>\n" +
    "</head>\n" +
    "<body>\n" +
    "    <h1>Hello, world!</h1>\n" +
    "</body>\n" +
    "</html>";

interface PresentationBoxProps {
    onClose: () => void;
}

export const PresentationBox: React.FC<PresentationBoxProps> = ({ onClose }) => {
    const navigator = useNavigate();

    const [file, setFile] = useState<File | null>(null);
    const [title, setTitle] = useState<string>("Безымянная презентация");
    const [dragActive, setDragActive] = useState(false);
    const [uploading, setUploading] = useState(false);
    const [htmlCode, setHtmlCode] = useState<string>(BASE_HTML_CODE);

    const handleDrag = useCallback((e: React.DragEvent) => {
        e.preventDefault();
        e.stopPropagation();
        if (e.type === 'dragenter' || e.type === 'dragover') {
            setDragActive(true);
        } else if (e.type === 'dragleave') {
            setDragActive(false);
        }
    }, []);

    const handleDrop = useCallback(async (e: React.DragEvent) => {
        e.preventDefault();
        e.stopPropagation();
        setDragActive(false);
        if (e.dataTransfer.files && e.dataTransfer.files[0]) {
            const droppedFile = e.dataTransfer.files[0];
            if (droppedFile.name.endsWith('.pptx')) {
                setFile(droppedFile);
                await handleFileUpload(droppedFile);
            } else {
                alert('Пожалуйста, загрузите файл в формате .pptx');
            }
        }
    }, []);

    const handleFileChange = async (e: React.ChangeEvent<HTMLInputElement>) => {
        if (e.target.files && e.target.files[0]) {
            const selectedFile = e.target.files[0];
            if (selectedFile.name.endsWith('.pptx')) {
                setFile(selectedFile);
                await handleFileUpload(selectedFile);
            } else {
                alert('Пожалуйста, загрузите файл в формате .pptx');
            }
        }
    };

    const handleFileUpload = async (fileToUpload: File) => {
        try {
            setUploading(true);
            const htmlCodeResponse = await uploadPptx(fileToUpload);
            setHtmlCode(htmlCodeResponse);
        } catch (error) {
            console.error("Ошибка при загрузке файла:", error);
            alert("Произошла ошибка при загрузке файла");
        } finally {
            setUploading(false);
        }
    };

    const handleCreate = async () => {
        const {id} = await uploadPrez(title, htmlCode);
        navigator(`/editor/${id}`);
        onClose();
    };


    return (
        <Box sx={{ width: 600, height: 636 }}>
            <Card
                sx={{
                    position: "relative",
                    width: 600,
                    height: 636,
                    bgcolor: "#f4f5f6",
                    borderRadius: "25px",
                    boxShadow: "10px 10px 4px #00000040"
                }}
            >
                <CardHeader
                    sx={{ pb: 0 }}
                    title={
                        <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "flex-start" }}>
                            <Typography
                                variant="h4"
                                sx={{
                                    width: "100%",
                                    fontFamily: "'Inter-Regular', Helvetica",
                                    fontWeight: 400,
                                    color: "#1b222c",
                                    textAlign: "center"
                                }}
                            >
                                Новая презентация
                            </Typography>
                            <IconButton sx={{ width: 40, height: 40 }} onClick={onClose}>
                                <CloseIcon sx={{ width: 30, height: 30 }} />
                            </IconButton>
                        </Box>
                    }
                />
                <Divider sx={{ mt: 2, maxWidth: "95%", ml: 2 }} />

                <CardContent sx={{ pt: 3, display: "flex", flexDirection: "column", gap: 3 }}>
                    <TextField
                        fullWidth
                        placeholder="Введите название презентации..."
                        sx={{
                            "& .MuiOutlinedInput-root": {
                                height: 37,
                                borderRadius: "10px",
                                borderColor: "#2f3a4c",
                                fontFamily: "'Inter-Regular', Helvetica",
                                fontSize: "1.2rem"
                            }
                        }}
                        onChange={(e) => setTitle(e.target.value.trim())}
                    />

                    <Box
                        sx={{
                            width: "94%",
                            height: 288,
                            border: `1px dashed ${dragActive ? '#098842' : 'black'}`,
                            backgroundColor: dragActive ? 'rgba(9, 136, 66, 0.1)' : 'transparent',
                            display: "flex",
                            flexDirection: "column",
                            alignItems: "center",
                            justifyContent: "center",
                            p: 2,
                            position: 'relative'
                        }}
                        onDragEnter={handleDrag}
                        onDragLeave={handleDrag}
                        onDragOver={handleDrag}
                        onDrop={handleDrop}
                    >
                        {uploading ? (
                            <>
                                <CircularProgress size={60} sx={{ mb: 2 }} />
                                <Typography
                                    sx={{
                                        fontFamily: "'Inter-Regular', Helvetica",
                                        fontWeight: 400,
                                        color: "#1b222c",
                                        fontSize: "1.2rem",
                                        textAlign: "center"
                                    }}
                                >
                                    Загрузка файла...
                                </Typography>
                            </>
                        ) : file ? (
                            <>
                                <CloudUploadIcon sx={{ fontSize: 60, color: "#098842", mb: 2 }} />
                                <Typography
                                    sx={{
                                        fontFamily: "'Inter-Regular', Helvetica",
                                        fontWeight: 400,
                                        color: "#1b222c",
                                        fontSize: "1.2rem",
                                        textAlign: "center",
                                        mb: 1
                                    }}
                                >
                                    {file.name}
                                </Typography>
                                <Typography
                                    sx={{
                                        fontFamily: "'Inter-Regular', Helvetica",
                                        fontWeight: 400,
                                        color: "#098842",
                                        fontSize: "1rem",
                                        textAlign: "center"
                                    }}
                                >
                                    {htmlCode == "" ? "Файл успешно загружен" : "Файл готов к загрузке"}
                                </Typography>
                            </>
                        ) : (
                            <>
                                <CloudUploadIcon sx={{ fontSize: 60, color: "#2f3a4c", mb: 2 }} />
                                <Typography
                                    sx={{
                                        fontFamily: "'Inter-Regular', Helvetica",
                                        fontWeight: 400,
                                        color: "#1b222c",
                                        fontSize: "1.2rem",
                                        textAlign: "center",
                                        mb: 5
                                    }}
                                >
                                    {dragActive ? 'Отпустите файл для загрузки' : 'Вы можете загрузить макет презентации в формате .pptx'}
                                </Typography>

                                <input
                                    accept=".pptx"
                                    style={{ display: 'none' }}
                                    id="upload-button-file"
                                    type="file"
                                    onChange={handleFileChange}
                                />
                                <label htmlFor="upload-button-file">
                                    <Button
                                        variant="contained"
                                        component="span"
                                        sx={{
                                            width: 179,
                                            height: 52,
                                            bgcolor: "#2f3a4c",
                                            borderRadius: "10px",
                                            fontFamily: "'Inter-SemiBold', Helvetica",
                                            fontWeight: 600,
                                            color: "white",
                                            fontSize: "1.2rem",
                                            mb: 3,
                                            "&:hover": {
                                                bgcolor: "#2f3a4c"
                                            }
                                        }}
                                    >
                                        Загрузить
                                    </Button>
                                </label>

                                <Typography
                                    sx={{
                                        fontFamily: "'Inter-Regular', Helvetica",
                                        fontWeight: 400,
                                        color: "#1b222c80",
                                        fontSize: "1.2rem",
                                        textAlign: "center"
                                    }}
                                >
                                    или перетащите файл сюда.
                                </Typography>
                            </>
                        )}
                    </Box>
                </CardContent>

                <CardActions
                    sx={{
                        position: "absolute",
                        bottom: 20,
                        width: "100%",
                        display: "flex",
                        justifyContent: "space-between",
                    }}
                >
                    <Button
                        variant="outlined"
                        sx={{
                            width: 179,
                            height: 42,
                            borderRadius: "25px",
                            borderColor: "#1b222c",
                            fontFamily: "'Inter-Regular', Helvetica",
                            fontWeight: 400,
                            color: "#1b222c",
                            fontSize: "1.2rem",
                            ml: 1
                        }}
                        onClick={onClose}
                    >
                        Отмена
                    </Button>

                    <Button
                        variant="contained"
                        sx={{
                            width: 179,
                            height: 42,
                            bgcolor: "#098842",
                            borderRadius: "25px",
                            fontFamily: "'Inter-SemiBold', Helvetica",
                            fontWeight: 600,
                            color: "white",
                            fontSize: "1.2rem",
                            "&:hover": {
                                bgcolor: "#098842"
                            },
                            mr: 3
                        }}
                        onClick={handleCreate}
                        disabled={uploading}
                    >
                        {uploading ? <CircularProgress size={24} color="inherit" /> : "Создать"}
                    </Button>
                </CardActions>
            </Card>
        </Box>
    );
}

export default PresentationBox;