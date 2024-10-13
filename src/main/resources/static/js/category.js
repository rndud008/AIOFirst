const categoryManage = async ()=>{

    const api = await fetch(`/admin/category`,
        {
            method:"GET"
        })

    window.location.href ='/admin/category';

}

const categoryList = async () =>{
    const api = await fetch('/admin/categories',{
        method:"GET"
    })

    window.location.href = `/admin/categories`;
}

const categorySaveForm = async ()=>{
    const api = await fetch(`/admin/category/save`, {
        method:'GET'
    })

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

    const api = await fetch(`/admin/category/save`, {
        method:'POST',
        body:new FormData(document.getElementById('categorySave'))
    })

    api.status === 200 && alert('저장 완료')
    saveButton.disabled =false;

    window.location.href ='/admin/categories';
}

const categoryModifyForm = async (id) =>{

    const api = await fetch(`/admin/category/modify/${id}`,
        {method: "GET"}
    )

    window.location.href =`/admin/category/modify/${id}`

}

const categoryModify = async ()=>{

    const title = document.getElementById('categoryName').value

    if(title.length <= 0) {
        alert('제목을 입력해주세요.')
        return;
    }

    const saveButton = document.getElementById('modifyButton');

    saveButton.disabled =true;

    const api = await fetch(`/admin/category/modify`,{
        method: 'PUT',
        body:new URLSearchParams(new FormData(document.getElementById('categoryModify')))
    })

    api.status === 200 && alert('수정 완료')

    saveButton.disabled =false;

    window.location.assign('/admin/categories')

}

const categoryRemove = async (param)=>{
    if(!confirm("삭제 하시겠습니까?")){
        return;
    }

    const api = await fetch(`/category/delete?id=${param}`,{
        method: 'DELETE',
    })

    api.status === 200 && alert('삭제 완료')
    window.location.href='/admin/categories'
}