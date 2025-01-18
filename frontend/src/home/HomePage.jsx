import axios from "axios";
import {useState} from "react";
import { useNavigate } from 'react-router-dom';
import { Link as RouterLink } from "react-router-dom";
import './home.css';
import './header.css';
import {EffectCoverflow,Pagination,Navigation} from 'swiper/modules';
import {Swiper,SwiperSlide} from 'swiper/react'
import './slajder.css'
import 'swiper/css/pagination';
import 'swiper/css/navigation';
import slide_image_1 from '../slike/img_1.jpg';
import slide_image_2 from '../slike/img_2.jpg';
import slide_image_3 from '../slike/img_3.jpg';
import slide_image_4 from '../slike/img_4.jpg';
import slide_image_5 from '../slike/img_5.jpg';
import slide_image_6 from '../slike/img_6.jpg';
import slide_image_7 from '../slike/img_7.jpg';
import React, {useEffect} from 'react';

const HomePage = () =>{

    const [user, setUser] = useState('');
    const [roles, setRoles] = useState([]);
    const [artistName, setArtistName] = useState('');
    const navigate = useNavigate();

    const handleInputChange = (event) => {
            setArtistName(event.target.value);
        };

    const handleFormSubmit = (event) => {
        event.preventDefault();
        handleSearch(artistName);
    };

    useEffect(() => {
        const token = localStorage.getItem('token');
        const headers = token
            ? {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`, 
              }
            : undefined;

        axios
            .get('http://localhost:8080/user-info', {
                withCredentials: true, 
                headers, 
            })
            .then((response) => {
                setUser(response.data.name); 
                setRoles(response.data.roles || []);
            })
            .catch((error) => {
                console.error('Error occurred: ', error);
            });
    }, [navigate]);

    const handleLogout = () => {
        localStorage.removeItem("token");
        localStorage.clear();
        window.location.href='http://localhost:8080/logout';
    }

    const handleSearch = (artistName) => {
        const token = localStorage.getItem("token");
            const headers = token
            ? {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`,
                }
            : undefined;

            fetch(`http://localhost:8080/concerts/artist?artist=${encodeURIComponent(artistName)}`, {
              method: "GET",
              headers: headers,
              credentials: 'include'
            })
              .then((response) => {
                if (!response.ok) {
                  throw new Error("No concerts found for this artist. Please try again.");
                }
                return response.json();
              })
              .then((concerts) => {
                if (concerts.length === 0) {
                  alert("No concerts found for this artist.");
                } else {
                  localStorage.setItem("concerts", JSON.stringify(concerts));
                  navigate("/ConcertDetails", { state: { concerts } });
                }
              })
              .catch((error) => {
                console.error("Error fetching concerts:", error);
                alert(error.message);
              });
    }

    const hasRole = (...requiredRoles) => {
        return requiredRoles.some(role => roles.includes(role));
    }

    const handleButtonClick = () =>console.log("Klik");
    const slideImages = [
        slide_image_1,
        slide_image_2,
        slide_image_3,
        slide_image_4,
        slide_image_5,
        slide_image_6,
        slide_image_7,
      ];
    return (
            <body className="bodyHome">
              <section className="h-wrapper">
                <div className="flexCenter paddings innerWidth h-container">
                  <img src="fakelogo.png" alt="logo" width={100} />

                  <div className="flexCenter h-menu">
                    <div className="centerText">
                      {user ? (
                        <span
                          className="WelcomeText"
                          style={{ fontFamily: "Poppins, sans-serif", fontWeight: "bold" }}
                        >
                          Welcome, {user}
                        </span>
                      ) : (
                        <span>Welcome, Guest</span>
                      )}
                    </div>

                    {hasRole('ADMIN', 'ARTIST') && (
                      <RouterLink to="/my-concerts" className="buttonLink">
                        <button className="buttonHP">My Concerts</button>
                      </RouterLink>
                    )}
                    {hasRole('ADMIN') && (
                      <RouterLink to="/manage-concerts" className="buttonLink">
                        <button className="buttonHP">Manage Concerts</button>
                      </RouterLink>
                    )}
                    {hasRole('ADMIN') && (
                      <RouterLink to="/manage-users" className="buttonLink">
                        <button className="buttonHP">Manage Users</button>
                      </RouterLink>
                    )}
                    {hasRole('ADMIN', 'ARTIST') && (
                      <RouterLink to="/addNewConcert" className="buttonLink">
                        <button className="buttonHP">Add Concert</button>
                      </RouterLink>
                    )}
                    {hasRole('USER', 'ADMIN', 'ARTIST') && (
                      <RouterLink to="/ticketmaster" className="buttonLink">
                        <button className="buttonHP">Search concerts</button>
                      </RouterLink>
                    )}
                    {hasRole('USER', 'ADMIN', 'ARTIST') && (
                      <RouterLink to="/concerts" className="buttonLink">
                        <button className="buttonHP">Concerts</button>
                      </RouterLink>
                    )}
                    {hasRole('SPOTIFY') && (
                      <RouterLink to="/favourites" className="buttonLink">
                        <button className="buttonHP">Favourites</button>
                      </RouterLink>
                    )}

                    {user ? (
                      <button className="buttonHP" onClick={handleLogout}>
                        Log Out
                      </button>
                    ) : (
                      <RouterLink to="/login" className="buttonLink">
                        <button className="buttonHP">Login</button>
                      </RouterLink>
                    )}
                  </div>
                </div>
              </section>

       <form method="post" onSubmit={handleFormSubmit}>
            <div className="searchBarWrapper">
                <input className="searchBar" type="text" placeholder="Search..." value={artistName} onChange={handleInputChange}/>
            </div>
       </form>

       <Swiper
                   effect={"coverflow"}
                   grabCursor={true}
                   centeredSlides={true}
                   loop={true}
                   slidesPerView={"auto"}
                   coverflowEffect={{
                     rotate: 0,
                     stretch: 0,
                     depth: 100,
                     modifier: 2.5,
                   }}
                   pagination={{ el: ".swiper-pagination", clickable: true }}
                   navigation={{
                     nextEl: ".swiper-button-next",
                     prevEl: ".swiper-button-prev",
                     clickable: true,
                   }}
                   modules={[EffectCoverflow, Pagination, Navigation]}
                   className="swiper_container"
                 >
                   {slideImages.map((image, index) => (
                     <SwiperSlide key={index}>
                       <div>
                         <img src={image} alt={`slide_image_${index + 1}`} />
                       </div>
                     </SwiperSlide>
                   ))}

                   <div className="slider-controler">
                     <div className="swiper-button-prev slider-arrow">
                       <ion-icon name="arrow-back-outline"></ion-icon>
                     </div>
                     <div className="swiper-button-next slider-arrow">
                       <ion-icon name="arrow-forward-outline"></ion-icon>
                     </div>
                     <div className="swiper-pagination"></div>
                   </div>
                 </Swiper>
               </body>
             );
           };
           export default HomePage;
