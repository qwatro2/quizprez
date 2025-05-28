import React from "react";
import {Box} from "@mui/material";
import styles from './style.module.css';
import backgroundImage from "../../assets/Background.svg";

interface BackgroundBoxProps {
    children?: React.ReactNode;
}

const BackgroundBox: React.FC<BackgroundBoxProps> = ({children}) => {
    return (
        <Box className={styles['background-box']} sx={{backgroundImage:`url(${backgroundImage})`}}>
            {children}
        </Box>
    )
}

export default BackgroundBox