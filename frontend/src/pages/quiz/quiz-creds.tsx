import React from "react";
import BackgroundBox from "../../components/backgroundbox/backgroundbox.tsx";
import { Box } from "@mui/material";

export const QuizCredsPage: React.FC = () => {
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
                        width: "80%",
                        maxWidth: "600px",
                        bgcolor: "background.paper",
                        borderRadius: "16px",
                        boxShadow: 24,
                        p: 4,
                        display: "flex",
                        flexDirection: "row",
                        justifyContent: "space-between"
                    }}
                >
                    <Box >

                    </Box>
                </Box>
            </Box>
        </BackgroundBox>
    );
};

export default QuizCredsPage;