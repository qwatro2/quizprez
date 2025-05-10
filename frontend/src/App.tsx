import {LoginPage} from "./pages/auth/login.tsx";
import {Route, Routes} from "react-router-dom";
import EditorPage from "./pages/EditorPage.tsx";

function App() {
    return (
        <Routes>
            <Route path="/" element={<LoginPage/>}/>
            <Route path="/editor" element={<EditorPage/>}/>
        </Routes>
    )
}

export default App