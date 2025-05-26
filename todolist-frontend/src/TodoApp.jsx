import {useState} from 'react'
import ListSelector from "./ListSelector.jsx";
import TodoList from "./TodoList.jsx";

function TodoApp() {
    const [currentListId, setCurrentListId] = useState(undefined)
    return <>
        <ListSelector currentListId={currentListId} setCurrentListId={setCurrentListId}/>
        <main><TodoList currentListId={currentListId}/></main>
    </>;
}

export default TodoApp
