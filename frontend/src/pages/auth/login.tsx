import NavBar from "../../components/navbar/navbar.tsx";
import BackgroundBox from "../../components/backgroundbox/backgroundbox.tsx";

export const LoginPage: React.FC = () => {
    return (
        <BackgroundBox>
            <NavBar needSearchLine={false} needButtonToSlides={true} slidesTitle={"Aboba"}></NavBar>
        </BackgroundBox>
    );
};