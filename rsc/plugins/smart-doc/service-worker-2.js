importScripts('/workbox-sw.js');

if (workbox) {
	console.log('✅ Workbox loaded successfully');

	workbox.setConfig({ debug: false });

	// ----- 🔹 PRECACHING -----
	// (Workbox replaces __WB_MANIFEST at build time if using workbox injectManifest)
	// You can remove this if you don’t use precaching
	// workbox.precaching.precacheAndRoute(self.__WB_MANIFEST || []);

	// ----- 🔹 STATIC FILES -----
	// JS, CSS, fonts
	workbox.routing.registerRoute(
		({ request }) => ['script', 'style', 'font'].includes(request.destination),
		new workbox.strategies.StaleWhileRevalidate({
			cacheName: 'static-assets',
		}),
	);

	// ----- 🔹 IMAGES -----
	// Images
	workbox.routing.registerRoute(
		({ request }) => request.destination === 'image',
		new workbox.strategies.CacheFirst({
			cacheName: 'images',
			plugins: [
				new workbox.expiration.ExpirationPlugin({
					maxEntries: 60,
					maxAgeSeconds: 30 * 24 * 60 * 60, // 30 Days
				}),
			],
		}),
	);

	// ----- 🔹 API UPLOADS -----
	workbox.routing.registerRoute(
		({ url }) => url.pathname.startsWith('/api/uploads/'),
		new workbox.strategies.CacheFirst({
			cacheName: 'uploads-cache',
			plugins: [
				new workbox.cacheableResponse.CacheableResponsePlugin({
					statuses: [0, 200],
				}),
				new workbox.expiration.ExpirationPlugin({
					maxEntries: 100,
					maxAgeSeconds: 7 * 24 * 60 * 60, // 7 days
				}),
			],
		}),
	);

	// ----- 🔹 API SEARCH -----
	workbox.routing.registerRoute(
		({ request, url }) =>
			(request.method === 'GET' &&
				url.pathname.startsWith('/api/') &&
				!url.pathname.startsWith('/api/uploads/')) ||
			(request.method === 'POST' &&
				url.pathname.match(/^\/api\/[^/]+\/search$/)),
		new workbox.strategies.NetworkFirst({
			cacheName: 'api',
			plugins: [
				new workbox.cacheableResponse.CacheableResponsePlugin({
					statuses: [200, 304],
				}),
				{
					requestWillFetch: async ({ request }) => {
						const cache = await caches.open('api');
						const cachedResponse = await cache.match(request);
						if (cachedResponse) {
							const etag = cachedResponse.headers.get('ETag');
							const lastModified = cachedResponse.headers.get('Last-Modified');
							const headers = new Headers(request.headers);
							if (etag) {
								headers.set('If-None-Match', etag);
							} else if (lastModified) {
								headers.set('If-Modified-Since', lastModified);
							}
							return new Request(request, { headers });
						}
						return request;
					},
					fetchDidSucceed: async ({ request, response }) => {
						if (response.status === 304) {
							const cache = await caches.open('api');
							const cachedResponse = await cache.match(request);
							return cachedResponse || response;
						}
						return response;
					},
				},
			],
		}),
	);

	// ----- 🔹 GRAPHQL -----
	workbox.routing.registerRoute(
		({ url }) => url.pathname.startsWith('/graphql'),
		new workbox.strategies.NetworkFirst({
			cacheName: 'graphql',
		}),
	);

	// ----- 🔹 APP SHELL (HTML) -----
	workbox.routing.registerRoute(
		({ request }) => request.mode === 'navigate',
		new workbox.strategies.StaleWhileRevalidate({
			cacheName: 'pages',
		}),
	);

	workbox.core.skipWaiting();
	workbox.core.clientsClaim();
}
