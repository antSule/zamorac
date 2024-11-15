import 'swiper/css';
import 'swiper/css/pagination'
import 'swiper/css/navigation'
import 'swiper/css/effect-coverflow';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import HomePage from './home/HomePage';
import LoginPage from './components/LoginPage'
import Concerts from './components/Concerts'
import Favourites from './components/Favourites'
import ProtectedRoute from './ProtectedRoute';

function App() {
  return (
    <div>
        <Router>
            <Routes>
                <Route path="/" element={<LoginPage />} />
                <Route path="/home" element={<ProtectedRoute><HomePage /></ProtectedRoute>} />
                <Route path="/concerts" element={<ProtectedRoute><Concerts /></ProtectedRoute>} />
            </Routes>
        </Router>
    </div>
  );
}

export default App;
