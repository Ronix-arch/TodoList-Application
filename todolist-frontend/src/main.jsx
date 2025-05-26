import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import TodoApp from './TodoApp.jsx'

createRoot(document.body).render(
  <StrictMode>
    <TodoApp/>
  </StrictMode>,
)
