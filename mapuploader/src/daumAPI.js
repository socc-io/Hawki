import Promise from 'q';

import postscribe from 'postscribe';

const deferred = Promise.defer();
let isLoading = false;

function load(APIKey) {
	if (!isLoading) {
		isLoading = true;
		// const url = `//dapi.kakao.com/v2/maps/sdk.js?appkey=${APIKey}`;
		const serviceURL = `//dapi.kakao.com/v2/maps/sdk.js?appkey=${APIKey}&libraries=services,clusterer`;
		const script = `
			<script type="text/javascript" src="${serviceURL}"></script>
		`;
		postscribe(window.document.head, script, {
			done: () => {
				deferred.resolve();
			},
			error:  (e) => {
				deferred.reject(e);
			},
		});
	}
	return deferred.promise;
}

const loadPromise = deferred.promise;

function getDaumMapAPI () {
	if(!window.daum.maps) {
		throw new Error('Daum Map not loaded yet!');
	}
	return window.daum.maps;
}

export default {
	load, loadPromise,
	getDaumMapAPI,
};