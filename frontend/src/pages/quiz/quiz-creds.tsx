import React from "react";
import BackgroundBox from "../../components/backgroundbox/backgroundbox.tsx";
import {Box, Divider, Typography} from "@mui/material";
import {useSearchParams} from "react-router-dom";
import qrUrl from "../../assets/qr.jpg";

interface QuizCredsPageProps {
    code: string;
    qrCode: string;
}

export const QuizCredsPage: React.FC = () => {
    const [searchParams] = useSearchParams();
    const code = searchParams.get("code") || "DEFAULT_CODE";
    const qrCode = searchParams.get("base64") || "";

    return (
        <QuizCredsPageContent code={code} qrCode={qrCode}/>
    );
};

export const QuizCredsPageContent: React.FC<QuizCredsPageProps> = ({code, qrCode}) => {
    return (
        <BackgroundBox>
            <Box
                sx={{
                    position: "absolute",
                    top: 0,
                    left: 0,
                    right: 0,
                    bottom: 0,
                    backdropFilter: "blur(1.5px)",
                    backgroundColor: "rgba(0, 0, 0, 0.05)",
                }}
            >
                <Box
                    sx={{
                        position: "absolute",
                        top: "50%",
                        left: "50%",
                        transform: "translate(-50%, -50%)",
                        width: "85%",
                        maxWidth: "750px",
                        height: "500px",
                        borderRadius: "16px",
                        backgroundColor: "white",
                        boxShadow: 24,
                        p: 4,
                        display: "flex",
                        flexDirection: "row",
                        justifyContent: "space-between",
                        gap: 4,
                    }}
                >
                    <Box sx={{flex: 1, display: "flex", flexDirection: "column", justifyContent: "center"}}>
                        <Typography variant="h4" component="h1" gutterBottom
                                    sx={{fontWeight: 'bold', textAlign: 'center'}}>
                            Присоединяйтесь к квизу по ссылке
                        </Typography>
                        <Typography variant="h5" component="p" gutterBottom sx={{
                            textAlign: 'center',
                            color: 'primary.main',
                            wordBreak: 'break-all',
                            fontSize: "2rem"
                        }}>
                            localhost:5432/quiz/join
                        </Typography>
                        <Typography variant="h4" component="p" sx={{textAlign: 'center', mt: 4}}>
                            и вводите код
                        </Typography>
                        <Typography variant="h3" component="p"
                                    sx={{textAlign: 'center', fontWeight: 'bold', color: '#098842', mt: 2}}>
                            {code}
                        </Typography>
                    </Box>

                    <Divider orientation="vertical" flexItem/>

                    <Box sx={{
                        flex: 1,
                        display: "flex",
                        flexDirection: "column",
                        alignItems: "center",
                        justifyContent: "center"
                    }}>
                        <Typography variant="h5" component="p" gutterBottom sx={{textAlign: 'center', mb: 3}}>
                            Или переходите
                        </Typography>
                        <Typography variant="h5" component="p" gutterBottom sx={{textAlign: 'center', mb: 3}}>
                            по QR-коду
                        </Typography>
                        <Box
                            component="img"
                            src={qrUrl}
                            sx={{
                                width: "200px",
                                height: "200px"
                            }}
                        />
                    </Box>
                </Box>
            </Box>
        </BackgroundBox>
    );
};

export default QuizCredsPage;