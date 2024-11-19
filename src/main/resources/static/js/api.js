const getResponse = async (url) => {

    const accessToken = await getAccessToken();

    const response = await fetch(url, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${accessToken}`
        }
    })

    return response;

}

const postResponse = async (url, data) => {

    const accessToken = await getAccessToken();

    const response = await fetch(url, {
        method: 'POST',
        body: data,
        headers: {
            'Authorization': `Bearer ${accessToken}`
        }
    })

    return response;

}

const postJsonResponse = async (url, data) => {

    const accessToken = await getAccessToken();

    const response = await fetch(url, {
        method: 'POST',
        body: data,
        headers: {
            'Authorization': `Bearer ${accessToken}`,
            'Content-Type': 'application/json'
        }
    })

    return response;

}


const deleteResponse = async (url) => {
    const accessToken = await getAccessToken();

    const response = await fetch(url, {
        method: 'DELETE',
        headers: {
            'Authorization': `Bearer ${accessToken}`
        }
    })

    return response;

}

const getAccessToken = async () => {
    const cookies = document.cookie.split('; ');
    let returnValue = '';

    cookies.forEach(cookie => {
        const [name, value] = cookie.split('=')

        if (name === "accessToken") {
            returnValue = value;
        }

    })

    return returnValue;
}

