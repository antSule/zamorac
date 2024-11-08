import 'swiper/css';
import 'swiper/css/pagination'
import 'swiper/css/navigation'
import 'swiper/css/effect-coverflow';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import HomePage from './home/HomePage';
import LoginPage from './login/LoginPage'


function App() {
  return (
    <div>
        <Router>
            <Routes>
                <Route path="/" element={<HomePage />} />
                <Route path="/login" element={<LoginPage />} />
            </Routes>
        </Router>
    </div>
  );
}

export default App;
