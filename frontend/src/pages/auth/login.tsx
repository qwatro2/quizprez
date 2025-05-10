import {Box} from "@mui/material";
import backgroundImage from "../../assets/Background.svg";

export const LoginPage: React.FC = () => {

    return (
        <Box
            sx={{
                backgroundImage: `url(${backgroundImage})`,
                backgroundSize: 'cover',
                backgroundPosition: 'center',
                backgroundRepeat: 'no-repeat',
                imageRendering: 'crisp-edges',
                top: 0,
                left: 0,
                right: 0,
                bottom: 0,
                position: 'fixed',
            }}
        />
    );
};