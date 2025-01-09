import axios from 'axios';
// 下载文件
export function downloadFile(fileUrl, name) {
    axios({
        url: '/api/file/download?url=' + fileUrl,
        method: 'get',
        responseType: 'blob'
    }).then(res => {
        const url = window.URL.createObjectURL(new Blob([res.data]))
        const link = document.createElement('a')
        link.style.display = 'none'
        link.href = url
        link.setAttribute('download', name)
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
    })
}
