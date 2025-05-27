import {AppBar, Box, Button, InputAdornment, List, ListItem, TextField, Typography} from "@mui/material";
import styles from "./style.module.css";
import titleUrl from "../../assets/Title.svg"
import burgerUrl from "../../assets/BurgerIcon.svg"
import searchUrl from "../../assets/SearchIcon.svg"
import changeUrl from "../../assets/ChangeIcon.svg"
import React, {useState} from "react";

interface NavbarProps {
    needButtonToSlides: boolean;
    needSearchLine: boolean;
    slidesTitle?: string;
    onTitleChange?: (newTitle: string) => void;
}

const NavBar: React.FC<NavbarProps> = ({needButtonToSlides, needSearchLine, slidesTitle, onTitleChange}) => {
    const [searchText, setSearchText] = useState("");
    const [isEditingTitle, setIsEditingTitle] = useState(false);
    const [editedTitle, setEditedTitle] = useState(slidesTitle || "");

    const handleTitleClick = () => {
        if (slidesTitle !== undefined) {
            setIsEditingTitle(true);
            setEditedTitle(slidesTitle);
        }
    };

    const handleTitleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setEditedTitle(e.target.value);
    };

    const handleTitleBlur = () => {
        setIsEditingTitle(false);
        if (onTitleChange && editedTitle !== slidesTitle) {
            onTitleChange(editedTitle);
        }
    };

    const handleKeyDown = (e: React.KeyboardEvent) => {
        if (e.key === 'Enter') {
            handleTitleBlur();
        }
    };

    return (
        <AppBar position="fixed" className={styles['navbar']}>
            <List className={styles['navbar-list']}>
                <ListItem className={styles['navbar-item']} sx={{
                    padding: '0 16px',
                    height: '100%',
                    marginRight: 'auto'
                }}>
                    <Box
                        component="img"
                        src={titleUrl}
                        sx={{
                            height: '60px',
                            width: 'auto',
                            maxHeight: '100%',
                            objectFit: 'contain',
                            display: 'block'
                        }}
                    />
                </ListItem>

                {needSearchLine && (
                    <ListItem className={styles['navbar-central-item']}>
                        <TextField
                            value={searchText}
                            onChange={(e) => setSearchText(e.target.value)}
                            placeholder="Искать среди всех проектов..."
                            variant="outlined"
                            size="small"
                            fullWidth
                            slotProps={{
                                input: {
                                    startAdornment: (
                                        <InputAdornment position="start">
                                            <Box
                                                component="img"
                                                src={searchUrl}
                                                sx={{width: 20, height: 20, paddingBottom: 0.5}}
                                            />
                                        </InputAdornment>
                                    ),
                                }
                            }}
                            sx={{
                                backgroundColor: "#FFFFFF",
                                borderRadius: '10px',
                                '& .MuiOutlinedInput-root': {
                                    height: '36px',
                                    paddingLeft: '8px',
                                },
                                '& .MuiOutlinedInput-input': {
                                    paddingBottom: '8px'
                                }
                            }}
                        >
                        </TextField>
                    </ListItem>
                )}

                {(slidesTitle !== undefined) && (
                    <ListItem className={styles['navbar-central-item']}>
                        <Box sx={{
                            display: "flex",
                            flexDirection: "row",
                            gap: "10px",
                            alignItems: "center",
                            cursor: "pointer",
                            backgroundColor: isEditingTitle ? "#FFFFFF" : "transparent",
                            borderRadius: "10px",
                            padding: isEditingTitle ? "8px 16px" : "0",
                            transition: "all 0.2s ease"
                        }} onClick={handleTitleClick}>
                            {isEditingTitle ? (
                                <TextField
                                    value={editedTitle}
                                    onChange={handleTitleChange}
                                    onBlur={handleTitleBlur}
                                    onKeyDown={handleKeyDown}
                                    autoFocus
                                    variant="standard"
                                    InputProps={{
                                        disableUnderline: true,
                                        style: {
                                            fontSize: "1.5rem",
                                            color: "#000000" // Черный цвет текста
                                        }
                                    }}
                                    sx={{
                                        '& .MuiInput-input': {
                                            padding: 0,
                                            color: "#000000" // Дублируем черный цвет
                                        }
                                    }}
                                />
                            ) : (
                                <>
                                    <Typography sx={{
                                        fontSize: "1.5rem",
                                        color: "#FFFFFF" // Черный цвет текста
                                    }}>
                                        {slidesTitle}
                                    </Typography>
                                    <Box
                                        component="img"
                                        src={changeUrl}
                                        sx={{width: 11, height: 11}}
                                    />
                                </>
                            )}
                        </Box>
                    </ListItem>
                )}

                {needButtonToSlides && (
                    <ListItem className={styles['navbar-item']}>
                        <Button
                            variant="outlined"
                            className={styles['navbar-button']}
                        >
                            <Typography sx={{textTransform: "none", fontSize: "1.2rem"}}>
                                К презентациям
                            </Typography>
                        </Button>
                    </ListItem>
                )}

                <ListItem className={styles['navbar-item']}>
                    <Button
                        variant="outlined"
                        className={styles['navbar-button']}
                    >
                        <Box
                            component="img"
                            src={burgerUrl}
                            sx={{
                                width: "16px",
                                height: "auto"
                            }}
                        />
                        <Typography sx={{textTransform: "none", fontSize: "1.2rem"}}>
                            Аккаунт
                        </Typography>
                    </Button>
                </ListItem>
            </List>
        </AppBar>
    )
}

export default NavBar;