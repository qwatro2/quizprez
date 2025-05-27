import {Route, Routes} from "react-router-dom";
import EditorPage from "./pages/editor/editor.tsx";
import HomePage from "./pages/home/home.tsx";

function App() {
    return (
        <Routes>
            <Route path="/" element={<HomePage/>}/>
            <Route path="/editor/:id" element={<EditorPage/>}/>
        </Routes>
    )
}

export default App