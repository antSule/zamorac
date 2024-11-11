import 'swiper/css';
import 'swiper/css/pagination'
import 'swiper/css/navigation'
import 'swiper/css/effect-coverflow';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import HomePage from './home/HomePage';
import LoginPage from './login/LoginPage'
import RegistrationPage from './registration/RegistrationPage'
import Favourites from './favourites/Favourites'

function App() {
  return (
    <div>
        <Router>
            <Routes>
                <Route path="/" element={<HomePage />} />
                <Route path="/login" element={<LoginPage />} />
                <Route path="/registration" element={<RegistrationPage />} />
                <Route path="/favourites" element={<Favourites />} />
            </Routes>
        </Router>
    </div>
  );
}

export default App;
