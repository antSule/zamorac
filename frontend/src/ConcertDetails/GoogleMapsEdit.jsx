import React, { useState, useRef } from "react";
import { GoogleMap, LoadScript, Marker, InfoWindow, Autocomplete } from "@react-google-maps/api";
import { useParams } from "react-router-dom";
import "./GoogleMapsAdd.css";

const mapContainerStyle = {
  width: "100%",
  height: "100vh",
};

const defaultCenter = {
  lat: 45.8150,
  lng: 15.9819,
};

const GoogleMapsEdit = () => {
  const [selectedLocation, setSelectedLocation] = useState(null);
  const [infoWindowContent, setInfoWindowContent] = useState(null);
  const [isFromSearch, setIsFromSearch] = useState(false);
  const { id } = useParams();
  const autocompleteRef = useRef(null);
  const BACKEND_URL = process.env.REACT_APP_BACKEND_URL;
  const FRONTEND_URL = process.env.REACT_APP_FRONTEND_URL;

  const handleMapClick = (event) => {
    const clickedLocation = event.latLng;
    const newLocation = { lat: clickedLocation.lat(), lng: clickedLocation.lng() };
    setSelectedLocation(newLocation);
    setInfoWindowContent(
      `<strong>Odabrano mjesto:</strong><br><span>Lat: ${newLocation.lat}, Lng: ${newLocation.lng}</span>`
    );
    setIsFromSearch(false);
  };

  const handleConfirmLocation = () => {
    if (selectedLocation) {
      const lat = selectedLocation.lat;
      const lng = selectedLocation.lng;
      const url = `${FRONTEND_URL}/edit-concert/${id}?lat=${lat}&lng=${lng}`;
      window.location.href = url;
    } else {
      alert("Molimo odaberite lokaciju prije potvrde!");
    }
  };

  const mapOptions = {
    mapId: "78e5ee292253a8e3",
    streetViewControl: false,
    mapTypeControl: false,
  };

  const handlePlaceChanged = () => {
    const place = autocompleteRef.current.getPlace();
    if (place.geometry) {
      const lat = place.geometry.location.lat();
      const lng = place.geometry.location.lng();
      const newLocation = { lat, lng };
      setSelectedLocation(newLocation);
      setInfoWindowContent(
        `<strong>Odabrano mjesto:</strong><br><span>Lat: ${lat}, Lng: ${lng}</span>`
      );
      setIsFromSearch(true);
    }
  };

  return (
    <LoadScript googleMapsApiKey={process.env.REACT_APP_GOOGLE_MAPS_API_KEY} libraries={["places"]}>
      <GoogleMap
        mapContainerStyle={mapContainerStyle}
        center={isFromSearch && selectedLocation ? selectedLocation : defaultCenter}
        zoom={selectedLocation ? 15 : 13}
        options={mapOptions}
        onClick={handleMapClick}
      >
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
          <InfoWindow position={selectedLocation} options={{ pixelOffset: new window.google.maps.Size(0, -30) }}>
            <div dangerouslySetInnerHTML={{ __html: infoWindowContent }} />
          </InfoWindow>
        )}
      </GoogleMap>

      <Autocomplete
        onLoad={(autocomplete) => {
          autocompleteRef.current = autocomplete;
        }}
        onPlaceChanged={handlePlaceChanged}
      >
        <input
          type="text"
          placeholder="Pretraži lokaciju"
          className="search-bar"
        />
      </Autocomplete>

      <button id="potvrda-lokacije-btn" className="potvrda-btn" onClick={handleConfirmLocation}>
        Confirm location
      </button>
    </LoadScript>
  );
};

export default GoogleMapsEdit;