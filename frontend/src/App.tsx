import {LoginPage} from "./pages/auth/login.tsx";
import {Route, Routes} from "react-router-dom";
import EditorPage from "./pages/editor/editor.tsx";

function App() {
    return (
        <Routes>
            <Route path="/" element={<LoginPage/>}/>
            <Route path="/editor" element={<EditorPage/>}/>
        </Routes>
    )
}

export default App