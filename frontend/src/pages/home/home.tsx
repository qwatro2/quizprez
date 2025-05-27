import React, {useEffect, useState} from "react";
import {Box, Button, Fab, Grid, Modal, Typography} from "@mui/material";
import NavBar from "../../components/navbar/navbar.tsx";
import sortUrl from "../../assets/SortIcon.svg";
import {Prez} from "../../data/models/Prez.tsx";
import {fetchPrezs} from "../../apis/prezApi.tsx";
import PresentationBox from "../../components/new-prez-modal/new-prez-modal.tsx";
import PrezCard from "../../components/prez-card/prez-card.tsx";

export const HomePage: React.FC = () => {
    const [prezs, setPrezs] = useState<Prez[]>([]);
    const [openModal, setOpenModal] = useState(false);

    const loadPrezs = async () => {
        try {
            const prezsFetch = await fetchPrezs();
            setPrezs(prezsFetch);
        } catch {
            console.error("Ошибка при загрузке презентаций")
        }
    }

    useEffect(() => {
        loadPrezs();
    }, []);

    const handleOpenModal = () => setOpenModal(true);
    const handleCloseModal = () => {
        setOpenModal(false);
        loadPrezs(); // Перезагружаем список после создания новой презентации
    };

    return (
        <Box sx={{
            backgroundColor: "#F4F5F6",
            minHeight: "100vh",
            display: "flex",
            flexDirection: "column"
        }}>
            <NavBar needSearchLine={true} needButtonToSlides={false}></NavBar>
            <Box sx={{
                display: "flex",
                flexDirection: "column",
                flexGrow: 1,
                padding: "0 24px",
                maxWidth: "1400px", // Ограничиваем максимальную ширину
                margin: "0 auto", // Центрируем контент
                width: "100%"
            }}>
                <Box sx={{
                    display: "flex",
                    alignItems: "center",
                    marginTop: "70px",
                    marginBottom: "20px",
                    justifyContent: "space-between" // Равномерное распределение элементов
                }}>
                    <Typography sx={{fontSize: "1.2rem"}}>
                        Недавние презентации
                    </Typography>
                    <Box sx={{display: "flex", alignItems: "center", gap: 2}}>
                        <Button
                            variant="contained"
                            onClick={handleOpenModal}
                            sx={{
                                backgroundColor: "#098842",
                                borderRadius: "45px",
                                boxShadow: "none",
                                height: "56px",
                                minWidth: "250px"
                            }}
                        >
                            <Typography sx={{textTransform: "none"}}>
                                Создать новую презентацию
                            </Typography>
                        </Button>
                        <Box component="img" src={sortUrl} sx={{width: "40px", height: "auto", cursor: "pointer"}}/>
                    </Box>
                </Box>

                <Box sx={{
                    backgroundColor: "#FFFFFF",
                    border: "1px solid rgba(27, 44, 44, 0.5)",
                    borderTopLeftRadius: "15px",
                    borderTopRightRadius: "15px",
                    flexGrow: 1,
                    padding: "24px",
                    minHeight: "500px",
                }}>
                    {prezs.length !== 0 ? (
                        <Grid container spacing={4}>
                            {prezs.map((prez: Prez) => (
                                <Grid item size={{xs:12, sm:6, md:4}} key={prez.id}>
                                    <PrezCard
                                        title={prez.title}
                                        htmlContent={prez.convertedHtml}
                                    />
                                </Grid>
                            ))}
                        </Grid>
                    ) : (
                        <Box sx={{
                            display: "flex",
                            justifyContent: "center",
                            alignItems: "center",
                            height: "300px"
                        }}>
                            <Typography variant="h6">
                                У вас ещё нет презентаций! Попробуйте создать первую
                            </Typography>
                        </Box>
                    )}
                </Box>
            </Box>

            <Fab
                color="primary"
                aria-label="add"
                sx={{
                    position: 'fixed',
                    bottom: 32,
                    right: 32,
                    backgroundColor: "#098842",
                    '&:hover': {
                        backgroundColor: "#098842",
                    }
                }}
                onClick={handleOpenModal}
            >
                <Typography sx={{fontSize:"3rem"}}>+</Typography>
            </Fab>

            <Modal
                open={openModal}
                onClose={handleCloseModal}
                sx={{
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                }}
            >
                <PresentationBox onClose={handleCloseModal}/>
            </Modal>
        </Box>
    )
}

export default HomePage;