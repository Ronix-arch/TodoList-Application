import {useContext, useEffect, useState} from 'react'
import {Api} from "./Context.jsx";

export default function TodoList({currentListId}){
    const api = useContext(Api);
    const [list, setList] = useState([]);
    const [newTodoName, setNewTodoName] = useState("New Todo");


    useEffect(() => {
        fetch(api + "/lists/" + currentListId)
            .then(response => {
                if (response.ok) return response.json();
                else throw new Error(response.statusText); })
            .then(result => {
                setList(result.todos);
            });
    }, [api, currentListId]);

    function createTodo() {
        fetch(api + "/" + currentListId + "/todos", {
            method: "POST",
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({task: newTodoName})
        })
            .then(response => {
                if (response.ok) return response.json();
                else throw new Error(response.statusText);
            })
            .then(result => {
                //let newList = list.slice();
                list.push(result);
                setList(list);
                // setCurrentListId(result.id)
            });
    }

    function updateTodo(id, newName, newStatus) {
        console.log("update")
        fetch(api + "/" + currentListId + "/todos/" + id, { method: "PUT", headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }, body: JSON.stringify({task: newName,
                                            status: newStatus}) })
            .then(response => {
                if (response.ok) return response.json();
                else throw new Error(response.statusText); })
            .then(result => {
                let index = list.findIndex(l => l.id === id);
               let newList = list.slice();
                newList[index] = result;
                setList(newList);
            });
    }
    function deleteTodo(id) {
        fetch(api + "/" + currentListId + "/todos/" + id, { method: "DELETE" })
            .then(response => {
                if (!response.ok) throw new Error(JSON.stringify(response)); })
            .then(() => {
                let index = list.findIndex(l => l.id === id);
                let newList = list.slice();
                newList.splice(index, 1);
                // if (id === currentListId) {
                //     setCurrentListId(undefined);
                // }
                setList(newList);
            });
    }


    return currentListId === undefined? (
        <article><h1>No list selected.</h1></article>) :
        ( <article>
        <h1>{list.name}</h1>
        <ul>

                {list?.map(todo => (
                    <li  key={todo.id} >
                    <div className="grid">
                        <button onClick={()=> updateTodo(todo.id, todo.task, todo.status === "DONE" ? "PENDING" : "DONE")}>
                            {todo.status === "DONE" ? "☑" : "☐"}</button>
                        <input defaultValue={todo.task } onChange={e =>{todo.task = e.target.value;}}/>
                        <button onClick={() => updateTodo(todo.id, todo.task, todo.status)}>Update</button>
                        <button className= "caution" onClick={() => deleteTodo(todo.id)}>Delete</button>
                    </div>
                    </li>
                ))}
        </ul>

        <div className="grid">
            <input value={newTodoName}  onChange={e => {setNewTodoName(e.target.value)}}/>
            <button onClick={createTodo}>Addtodo</button>
        </div>
    </article>);


}