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

    useEffect(() => {
    const fetchUserInfo = async () => {
        try {
            const response = await axios.get('https://ticketmestarbackend-yqpn.onrender.com/api/user-info', { withCredentials: true });
            console.log(response.data);
            setUser(response.data);
        } catch (error) {
            console.error('Error occurred: ', error);
        }
    };
    fetchUserInfo();
}, []);

    const handleLogout = () => {
        window.location.href='https://ticketmestarbackend-yqpn.onrender.com/api/logout';
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
    return(
    <>
       <section className=" h-wrapper">
        <div className="flexCenter paddings innerWidth h-container">
            <img src="fakelogo.png" alt="logo"width = {100} />

             <div className="flexCenter h-menu">
                 <div className="centerText">
                     {user ? (
                         <span className="WelcomeText">
                         Welcome, {user.name}
                        </span>
                     ) :
                     <span></span>}
                 </div>
                 <RouterLink to="/concerts">
                     Concerts
                 </RouterLink>
                 <RouterLink to="/favourites">
                     Favourites
                 </RouterLink>
                    {user ? (
                        <button className="button" onClick={handleLogout}>
                            Log Out
                        </button>
                    ) : (
                        <RouterLink to="/login" className="button">
                            Login
                        </RouterLink>
                    )}
             </div>
        </div>
       </section>

       <div className="searchBarWrapper">
         <input className="searchBar" type="text" placeholder="Search..." />
       </div>

       <Swiper
        effect={'coverflow'}
        grabCursor={true}
        centeredSlides={true}
        loop={true}
        slidesPerView={'auto'}
        coverflowEffect={{
          rotate: 0,
          stretch: 0,
          depth: 100,
          modifier: 2.5,
        }}
        pagination={{ el: '.swiper-pagination', clickable: true }}
        navigation={{
          nextEl: '.swiper-button-next',
          prevEl: '.swiper-button-prev',
          clickable: true,
        }}
        modules={[EffectCoverflow, Pagination, Navigation]}
        className="swiper_container"
      >
       {slideImages.map((image, index) => (
      <SwiperSlide key={index}>
      <div>
          <img src={image} alt={`slide_image_${index + 1}`} />
          <button onClick={() => handleButtonClick(index + 1)}>
              {/* SVG ikona spremanja */}
              <svg
                  width="24"
                  height="24"
                  viewBox="0 0 24 24"
                  fill="none"
                  xmlns="http://www.w3.org/2000/svg"
              >
                  <path
                      d="M17 3H7a2 2 0 00-2 2v16l7-3 7 3V5a2 2 0 00-2-2z"
                      stroke="#000" /* Crna boja obrisa */
                      strokeWidth="1.5"
                      fill="none"
                  />
              </svg>
          </button>
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
    </>
    );
};
export default HomePage;
