import React from "react";
import {
    Box,
    Card,
    CardHeader,
    CardContent,
    CardActions,
    IconButton,
    Divider,
    TextField,
    Button,
    Typography
} from "@mui/material";
import { Close as CloseIcon } from "@mui/icons-material";

export const PresentationBox: React.FC = () => {
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
                            <IconButton sx={{ width: 40, height: 40 }}>
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
                                fontSize: "1.2rem" // ~24px
                            }
                        }}
                    />

                    <Box
                        sx={{
                            width: "94%",
                            height: 288, // 72 * 4 (since MUI uses 8px base)
                            border: "1px dashed black",
                            display: "flex",
                            flexDirection: "column",
                            alignItems: "center",
                            justifyContent: "center",
                            p: 2
                        }}
                    >
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
                            Вы можете загрузить макет презентации в формате .pptx
                        </Typography>

                        <Button
                            variant="contained"
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
                    >
                        Создать
                    </Button>
                </CardActions>
            </Card>
        </Box>
    );
}

export default PresentationBox;