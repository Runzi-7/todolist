import axios from 'axios'

const http = axios.create({
  baseURL: '/api',
  timeout: 8000
})

export async function fetchTasks() {
  const response = await http.get('/tasks')
  return response.data
}
