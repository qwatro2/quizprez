import React, {useEffect, useState} from "react";
import {Box, Button, Fab, Modal, Typography} from "@mui/material";
import NavBar from "../../components/navbar/navbar.tsx";
import sortUrl from "../../assets/SortIcon.svg";
import {Prez} from "../../data/models/Prez.tsx";
import {fetchPrezs} from "../../apis/prezApi.tsx";
import PresentationBox from "../../components/new-prez-modal/new-prez-modal.tsx";
import PrezCard from "../../components/prez-card/prez-card.tsx";

export const HomePage: React.FC = () => {
    const [prezs, setPrezs] = useState<Prez[]>([]);
    const [openModal, setOpenModal] = useState(false); // Состояние для управления модальным окном

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
    const handleCloseModal = () => setOpenModal(false);

    return (
        <Box sx={{
            backgroundColor: "#F4F5F6",
            minHeight: "100vh", // Занимает всю высоту viewport
            display: "flex",
            flexDirection: "column"
        }}>
            <NavBar needSearchLine={true} needButtonToSlides={false}></NavBar>
            <Box sx={{
                display: "flex",
                flexDirection: "column",
                flexGrow: 1 // Занимает все доступное пространство
            }}>
                <Box sx={{
                    display: "flex",
                    flexDirection: "row",
                    marginTop: "70px",
                }}>
                    <Typography sx={{fontSize: "1.2rem", marginLeft: "10px"}}>
                        Недавние презентации
                    </Typography>
                    <Button variant="contained" onClick={handleOpenModal} sx={{
                        backgroundColor: "#098842",
                        borderRadius: "45px",
                        boxShadow: "none",
                        marginLeft: "25%",
                        marginBottom: "15px",
                        maxHeight: "56px",
                    }}>
                        <Typography sx={{textTransform: "none"}}>
                            Создать новую презентацию
                        </Typography>
                    </Button>
                    <Box component="img" src={sortUrl}
                         sx={{marginLeft: "38%", marginBottom: "15px", width: "40px", height: "auto"}}/>
                </Box>
                <Box sx={{
                    backgroundColor: "#FFFFFF",
                    borderTop: "1px solid rgba(27, 44, 44, 0.5)",
                    borderLeft: "1px solid rgba(27, 44, 44, 0.5)",
                    borderRight: "1px solid rgba(27, 44, 44, 0.5)",
                    borderTopLeftRadius: "15px",
                    borderTopRightRadius: "15px",
                    flexGrow: 1, // Занимает все оставшееся пространство
                }}>
                    {prezs.length !== 0 ? (
                        <Box sx={{
                            display: "grid",
                            justifyContent: "center",
                            gridTemplateColumns: "repeat(auto-fill, minmax(250px, 1fr))"
                        }}>
                            {prezs.map((prez: Prez) => (
                                <PrezCard key={prez.id} title={prez.title} htmlContent={prez.convertedHtml}></PrezCard>
                            ))}
                        </Box>
                    ) : (
                        <Box sx={{display: "flex", justifyContent: "center", alignItems: "center", marginTop: "200px"}}>
                            <Typography>У вас ещё нет презентаций! Попробуйте создать первую</Typography>
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
            >
                <Typography sx={{fontSize: "3rem"}} onClick={handleOpenModal}>+</Typography>
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