import {Route, Routes} from "react-router-dom";
import EditorPage from "./pages/editor/editor.tsx";
import HomePage from "./pages/home/home.tsx";
import {QuizCredsPage} from "./pages/quiz/quiz-creds.tsx";

function App() {
    return (
        <Routes>
            <Route path="/" element={<HomePage/>}/>
            <Route path="/editor/:id" element={<EditorPage/>}/>
            <Route path="/quiz/creds" element={<QuizCredsPage/>}/>
        </Routes>
    )
}

export default App