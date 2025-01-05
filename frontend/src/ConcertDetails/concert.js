document.addEventListener('DOMContentLoaded', function() {
    const concerts = JSON.parse(localStorage.getItem('concerts'));

    if (!concerts || concerts.length === 0) {
        alert("No concerts found.");
        return;
    }

    const concertsContainer = document.getElementById('concerts-container');
    concertsContainer.innerHTML = '';

    concerts.forEach(concert => {
        const concertDiv = document.createElement('div');
        concertDiv.classList.add('concert');

        concertDiv.innerHTML = `
            <div class="concert-image">
                <img src="${concert.imageUrl || 'default-image.jpg'}" alt="${concert.name}">
            </div>
            <div class="concert-details">
                <h3>${concert.event}</h3>
                <p><strong>Performer:</strong> ${concert.performer}</p>
                <p><strong>City:</strong> ${concert.city}</p>
                <p><strong>Date:</strong> ${concert.date}</p>
                <p><strong>Time:</strong> ${concert.time}</p>
                <p><strong>Venue:</strong> ${concert.venue}</p>
                <a href="${concert.url}" target="_blank">Buy tickets</a>
            </div>
        `;

        concertsContainer.appendChild(concertDiv);
    });
});