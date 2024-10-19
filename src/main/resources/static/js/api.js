const getRespsonse = async (url) => {

    const accessToken = getAccessToken();

    const response = await fetch(`${url}`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${accessToken}`
        }
    })

    if (response.statusText === 'ERROR_ACCESS_TOKEN') {

        await fetchRefreshToken(accessToken);

        const newAccessToken = getAccessToken();

        const retryResponse = await fetch(`${url}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${newAccessToken}`
            }
        })

        return retryResponse;
    }


    return response;

}

const postResponse = async (url, data) => {

    const accessToken = getAccessToken();

    try {
        const response = await fetch(`${url}`, {
            method: 'POST',
            body: data,
            headers: {
                'Authorization': `Bearer ${accessToken}`
            }
        })

        return response;

    } catch (e) {
        if (e.message === 'ERROR_ACCESS_TOKEN') {

            await fetchRefreshToken(accessToken);

            const newAccessToken = getAccessToken();

            const retryResponse = await fetch(`${url}`, {
                method: 'POST',
                body: data,
                headers: {
                    'Authorization': `Bearer ${newAccessToken}`
                }
            })

            return retryResponse;

        }
    }

}

const deleteResponse = async (url,) => {
    const accessToken = await getAccessToken();

    const response = await fetch(`${url}`, {
        method: 'DELETE',
        headers: {
            'Authorization': `Bearer ${accessToken}`
        }
    })

    return response;

}

const getAccessToken = () => {
    const auth = localStorage.getItem('Authorization')

    const json = JSON.parse(auth);

    return json.accessToken;
}

const getRefreshToken = () => {

    const auth = localStorage.getItem('Authorization')

    const json = JSON.parse(auth);

    return json.refreshToken;

}

const fetchRefreshToken = async (accessToken) => {
    const refreshToken = getRefreshToken();

    const response = await fetch(`/refresh?refreshToken=${refreshToken}`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${accessToken}`
        }
    })

    const data = await response.json();

    const newAccessToken = data.accessToken
    const newRefreshToken = data.refreshToken;

    document.cookie = `accessToken=${newAccessToken}; path=/;`;

    const auth = localStorage.getItem('Authorization')

    const json = JSON.parse(auth);

    json.accessToken = newAccessToken;
    json.refreshToken = newRefreshToken;

    localStorage.setItem('Authorization', JSON.stringify(json));
}