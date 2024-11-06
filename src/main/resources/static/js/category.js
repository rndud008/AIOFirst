

const categoryManage = async ()=>{

    const response = await getResponse(`/admin/category`)

    window.location.href ='/admin/category';

}

const categoryList = async () =>{
    const response = await getResponse('/admin/categories')

    window.location.href = `/admin/categories`;
}

const categorySaveForm = async ()=>{
    const api = await getResponse(`/admin/category/save`)

    window.location.href ='/admin/category/save';
}

const categorySave = async () =>{
    const title = document.getElementById('categoryName').value

    if(title.length <= 0) {
        alert('제목을 입력해주세요.')
        return;
    }

    const saveButton = document.getElementById('saveButton');

    saveButton.disabled =true;

    const data = new FormData(document.getElementById('categorySave'))

    const response = await postResponse(`/admin/category/save`, data)

    console.log(response)

    response.status === 200 && alert('저장 완료')

    saveButton.disabled =false;

    window.location.href ='/admin/categories';
}

const categoryModifyForm = async (id) =>{

    const response = await getResponse(`/admin/category/modify/${id}`)

    window.location.href =`/admin/category/modify/${id}`

}

const categoryModify = async ()=>{

    const title = document.getElementById('categoryName').value

    if(title.length <= 0) {
        alert('제목을 입력해주세요.')
        return;
    }

    const modifyButton = document.getElementById('modifyButton');

    modifyButton.disabled =true;

    const data = new FormData(document.getElementById('categoryModify'))

    const api = await postResponse(`/admin/category/modify`, data)

    console.log(api)

    api.status === 200 && alert('수정 완료')

    modifyButton.disabled =false;

    window.location.assign('/admin/categories')

}

const categoryRemove = async (param)=>{

    if(!confirm("삭제 하시겠습니까?")){
        return;
    }

    const api = await deleteResponse(`/admin/category/delete?id=${param}`)


    api.status === 200 && alert('삭제 완료')
    window.location.href='/admin/categories'
}