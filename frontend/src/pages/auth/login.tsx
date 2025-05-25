import React from 'react';
import BackgroundBox from "../../components/backgroundbox/backgroundbox.tsx";
import NavBar from "../../components/navbar/navbar.tsx";

export const LoginPage: React.FC = () => {
    return (
        <BackgroundBox>
            <NavBar needSearchLine={false} needButtonToSlides={true} slidesTitle={"Aboba"}/>
        </BackgroundBox>
    );
};