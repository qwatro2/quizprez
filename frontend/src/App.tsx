import {LoginPage} from "./pages/auth/login.tsx";
import {Route, Routes} from "react-router-dom";

function App() {
    return (
        <Routes>
            <Route path="/" element={<LoginPage/>}/>
        </Routes>
    )
}

export default App