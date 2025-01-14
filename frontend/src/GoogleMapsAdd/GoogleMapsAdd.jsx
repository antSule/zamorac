import React, { useState } from "react";
import { GoogleMap, LoadScript, Marker, InfoWindow } from "@react-google-maps/api";
import "./GoogleMapsAdd.css";

const mapContainerStyle = {
  width: "100%",
  height: "100vh",
};

const defaultCenter = {
  lat: 45.8150,
  lng: 15.9819,
};

const GoogleMapsAdd = () => {
  const [selectedLocation, setSelectedLocation] = useState(null);
  const [infoWindowContent, setInfoWindowContent] = useState(null);

  const handleMapClick = (event) => {
    const clickedLocation = event.latLng;
    setSelectedLocation(clickedLocation);
    setInfoWindowContent(
      `<strong>Odabrano mjesto:</strong><br><span>Lat: ${clickedLocation.lat()}, Lng: ${clickedLocation.lng()}</span>`
    );
  };

  const handleConfirmLocation = () => {
    if (selectedLocation) {
      const lat = selectedLocation.lat();
      const lng = selectedLocation.lng();
      const url = `http://https://ticketmestarfrontend-c9vl.onrender.com/addNewConcert?lat=${lat}&lng=${lng}`;
      window.location.href = url;
    } else {
      alert("Molimo odaberite lokaciju prije potvrde!");
    }
  };

  return (
    <LoadScript
      googleMapsApiKey={process.env.REACT_APP_GOOGLE_MAPS_API_KEY}
    >
      <GoogleMap
        mapContainerStyle={mapContainerStyle}
        center={defaultCenter}
        zoom={13}
        onClick={handleMapClick}
      >
        {}
        {selectedLocation && (
          <Marker
            position={selectedLocation}
            icon={{
              url: "http://maps.google.com/mapfiles/ms/icons/red-dot.png",
              scaledSize: new window.google.maps.Size(40, 40),
            }}
          />
        )}

        {selectedLocation && (
          <InfoWindow
            position={selectedLocation}
            options={{
              pixelOffset: new window.google.maps.Size(0, -30),
            }}
          >
            <div dangerouslySetInnerHTML={{ __html: infoWindowContent }} />
          </InfoWindow>
        )}
      </GoogleMap>

      {}
      <button
        id="potvrda-lokacije-btn"
        className="potvrda-btn"
        onClick={handleConfirmLocation}
      >
        Confirm location
      </button>
    </LoadScript>
  );
};

export default GoogleMapsAdd;