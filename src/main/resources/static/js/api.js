const getRespsonse = async (url) => {

    const accessToken = await getAccessToken();

    console.log(accessToken)

    const response = await fetch(`${url}`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${accessToken}`
        }
    })

    return response;

}

const postResponse = async (url, data) => {

    const accessToken = await getAccessToken();
    console.log(accessToken)

        const response = await fetch(`${url}`, {
            method: 'POST',
            body: data,
            headers: {
                'Authorization': `Bearer ${accessToken}`
            }
        })

        return response;



}

const deleteResponse = async (url,) => {
    const accessToken = await getAccessToken();
    console.log(accessToken)

    const response = await fetch(`${url}`, {
        method: 'DELETE',
        headers: {
            'Authorization': `Bearer ${accessToken}`
        }
    })

    return response;

}

const getAccessToken = async () => {
    const cookies = document.cookie.split('; ');
    console.log(cookies)

    cookies.forEach(cookie =>{
        const [name,value] = cookie.split('=')
        console.log("name:",name,"value:",value)
        if(name === "accessToken"){
            return decodeURIComponent(value);
        }
    })

    return "";
}

