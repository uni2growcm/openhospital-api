if ('serviceWorker' in navigator) {
	navigator.serviceWorker
		.register('/service-worker.js')
		.then((registration) => {
			console.log('Service Worker registered:', registration);
		})
		.catch((error) => {
			console.error('Service Worker registration failed:', error);
		});
} else {
	console.warn('Service Workers are not supported in this browser.');
}
