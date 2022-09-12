import request from '@/utils/request'

export function fetchList(query) {
  return request({
    url: '/job-info/page',
    method: 'get',
    params: query
  })
}

export function updateEnableStatus(id, enable) {
  return request({
    url: '/job-info/update/enable-status',
    method: 'post',
    data: {
      id: id,
      enable: enable
    }
  })
}

export function execute(data) {
  return request({
    url: '/job-info/execute',
    method: 'post',
    data: data
  })
}
