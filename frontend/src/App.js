import 'swiper/css';
import 'swiper/css/pagination'
import 'swiper/css/navigation'
import 'swiper/css/effect-coverflow';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import HomePage from './home/HomePage';
import LoginPage from './components/LoginPage'
import RegistrationPage from "./components/RegistrationPage";
import VerifyPage from "./components/VerifyPage";
import SelectRolePage from "./components/SelectRolePage";
import Concerts from './components/Concerts'
import MyConcerts from './components/MyConcerts'
import Favourites from './components/Favourites'
import ProtectedRoute from './ProtectedRoute';
import Ticketmaster from './TicketMaster/ticketmaster';
import GoogleMapsAdd from './GoogleMapsAdd/GoogleMapsAdd';
import GoogleMapsTicket from './GoogleMapsTicket/GoogleMapsTicket';
import ConcertDetails from './ConcertDetails/ConcertDetails';
import AddNewConcert from './AddNewConcert/AddNewConcert';
import ManageUsers from './components/ManageUsers';
import ManageConcerts from './components/ManageConcerts';
import ArtistConcerts from './components/ArtistConcerts';
import EditConcert from './ConcertDetails/EditConcert';
import EditConcertADMIN from './ConcertDetails/EditConcertADMIN';
import GoogleMapsEdit from './ConcertDetails/GoogleMapsEdit';
import GoogleMapsEditAdmin from './ConcertDetails/GoogleMapsEditAdmin';

function App() {
  return (
    <div>
        <Router>
            <Routes>
                <Route path="/" element={<LoginPage />} />
                <Route path="/register" element={<RegistrationPage />} />
                <Route path="/verify" element={<VerifyPage />} />
                <Route path="/select-role" element={<SelectRolePage />} />
                <Route path="/home" element={<ProtectedRoute><HomePage /></ProtectedRoute>} />
                <Route path="/concerts" element={<ProtectedRoute><Concerts /></ProtectedRoute>} />
                <Route path="/favourites" element={<ProtectedRoute><Favourites /></ProtectedRoute>} />
                <Route path="/my-concerts" element={<ProtectedRoute><MyConcerts /></ProtectedRoute>} />
                <Route path="/ticketmaster" element ={<ProtectedRoute><Ticketmaster/></ProtectedRoute>}/>
                <Route path="/google-maps" element ={<ProtectedRoute><GoogleMapsAdd/></ProtectedRoute>}/>
                <Route path="/google-maps-ticket" element ={<ProtectedRoute><GoogleMapsTicket/></ProtectedRoute>}/>
                <Route path="/ConcertDetails" element ={<ProtectedRoute><ConcertDetails/></ProtectedRoute>}/>
                <Route path="/addNewConcert" element ={<ProtectedRoute><AddNewConcert/></ProtectedRoute>}/>
                <Route path="/manage-users" element ={<ProtectedRoute><ManageUsers/></ProtectedRoute>}/>
                <Route path="/manage-concerts" element ={<ProtectedRoute><ManageConcerts/></ProtectedRoute>}/>
                <Route path="/favourites" element={<ProtectedRoute><Favourites /></ProtectedRoute>} />
                <Route path="/concerts/artist/:artistId" element={<ProtectedRoute><ArtistConcerts /></ProtectedRoute>} />
                <Route path="/edit-concert/:id" element={<ProtectedRoute><EditConcert /></ProtectedRoute>}/>
                <Route path="/edit-concert-admin/:id" element={<ProtectedRoute><EditConcertADMIN /></ProtectedRoute>} />
                <Route path="/google-maps-edit/:id" element={<ProtectedRoute><GoogleMapsEdit /></ProtectedRoute>}/>
                <Route path="/google-maps-edit-admin/:id" element={<ProtectedRoute><GoogleMapsEditAdmin /></ProtectedRoute>}/>
            </Routes>
        </Router>
    </div>
  );
}

export default App;
