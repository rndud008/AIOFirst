

const productManage = async () => {
    const api = await getRespsonse(`/admin/product`)

    window.location.href = '/admin/product'
}

const productValidation = () => {

}

const productSaveForm = async () => {
    const api = await getRespsonse(`/admin/product/save`)

    window.location.href = '/admin/product/save'
}

const productSave = async () => {

    const button = document.getElementById('saveButton');
    button.disabled = true;

    const data = new FormData(document.getElementById('productSave'))

    console.log(data)

    const api = await postResponse('/admin/product/save',  data)

    if (api.status === 200) {
        alert('저장완료')
        window.location.href = '/admin/product'
    } else {
        alert('저장 실패')
    }

    button.disabled = false;

}

const productSearch = async () => {

    const api = await getRespsonse(`/admin/product/menu`)

    window.location.href = '/admin/product/menu'

}

const getQueryParmas = () => {
    const params = new URLSearchParams(window.location.search);
    const page = params.get('page') ? parseInt(params.get('page')) : 1;
    const size = params.get('size') ? parseInt(params.get('size')) : 10;

    return {page, size}
}

const productSearchList = async (categoryId, subcategory = 0, currentPage = 0) => {

    if (categoryId === null) {
        const currentUrl = window.location.href;
        const start = currentUrl.lastIndexOf("/", currentUrl.indexOf('?')) + 1;
        const last = currentUrl.indexOf('?')
        categoryId = parseInt(currentUrl.substring(start, last));
        const params = new URLSearchParams(window.location.search);
        subcategory = params.get('subcategory') ? parseInt(params.get('subcategory')) : 0;
    }

    const {page, size} = await getQueryParmas();

    const queryParams = new URLSearchParams({
        page: currentPage === 0 ? page : parseInt(currentPage),
        size: size,
        subcategory: subcategory
    });

    const url = `/admin/product/menu/${categoryId}?${queryParams.toString()}`;

    const api = await getRespsonse(url)

    window.location.href = `${url}`

}

const productModifyForm = async (id) => {

    const api = await getRespsonse(`/admin/product/modify/${id}`)

    window.location.href = `/admin/product/modify/${id}`
}

const productModify = async (id) => {

    const button = document.getElementById('productModify');
    button.disabled = true;

    const data = new FormData(document.getElementById('productModify'))

    const api = await postResponse(`/admin/product/modify/${id}`, data)

    if (api.status === 200) {
        alert('수정 완료')
        window.location.href = '/admin/product'
    } else {
        alert('수정 실패')
    }

    button.disabled = false;

}

const productVariantForm = async (id) => {

    const api = await getRespsonse(`/admin/product/variant/${id}`)

    window.location.href = `/admin/product/variant/${id}`


}

const productVariantModify = async (id) => {

    const data = new FormData(document.getElementById('productVariantForm'))

    const button = document.getElementById('variantModifyButton');
    button.disabled = true;


    const api = await postResponse(`/admin/product/variant/${id}`,  data)

    button.disabled = false;

    window.location.href = '/admin/product'


}

const existVariantItemRemove = (id) => {
    if (!confirm('삭제하시겠습니까?')) return;

    const existVariantItem = document.getElementById(id);

    if (existVariantItem) {
        existVariantItem.remove();
    }
}

const newVariantItemCreate = () => {

    const newVariantCount = document.querySelectorAll('.new-variant-item').length;
    const newIndex = newVariantCount;

    const newVariantItem = document.createElement('div');
    newVariantItem.classList.add('new-variant-item');

    newVariantItem.innerHTML = `
                <div id="new-item-${newIndex}">
                    <label for="newSize-${newIndex}">상품 사이즈</label>
                    <input style="width: 100px" type="text" id="newSize-${newIndex}" name="newVariants[${newIndex}].size" placeholder="ex) L"/>

                    <label for="newColor-${newIndex}">상품 색상</label>
                    <input style="width: 100px" type="text" id="newColor-${newIndex}" name="newVariants[${newIndex}].color" placeholder="ex) 빨강"/>

                    <label for="newStockQuantity-${newIndex}">재고량</label>
                    <input style="width: 100px" type="number" id="newStockQuantity-${newIndex}" name="newVariants[${newIndex}].stockQuantity" placeholder="초기 수량을 입력해 주세요."/>

                    <label th:for="newPrice-${newIndex}">추가 금액</label>
                    <input style="width: 100px" type="number" th:id="newPrice-${newIndex}" name="newVariants[${newIndex}].price" value="0" />

                    <button type="button" onclick="existVariantItemRemove('new-item-${newIndex}')">삭제</button>

                </div>
            `;

    newIndex === 0 && document.querySelector('.exist-variant-item').parentNode.appendChild(newVariantItem) ||
    document.querySelector('.new-variant-item').parentNode.appendChild(newVariantItem)

}
