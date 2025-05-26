import {useContext, useEffect, useState} from 'react'
import {Api} from "./Context.jsx";

export default function ListSelector({currentListId, setCurrentListId}) {
    const api = useContext(Api);
    const [lists, setLists] = useState([]);
    const [newListName, setNewListName] = useState("New List");

    useEffect(() => {
        fetch(api + "/lists")
            .then(response => {
                if (response.ok) return response.json();
                else throw new Error(response.statusText); })
            .then(result => {
                setLists(result);
            });
    }, [api]);

    function createList() {
        fetch(api + "/lists", { method: "POST", headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }, body: JSON.stringify({name: newListName}) })
            .then(response => {
                if (response.ok) return response.json();
                else throw new Error(response.statusText); })
            .then(result => {
                let newLists = lists.slice();
                newLists.push(result);
                setLists(newLists);
                setCurrentListId(result.id)
            });
    }

    function renameList(id, newName) {
        console.log("rename")
        fetch(api + "/lists/" + id, { method: "PUT", headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }, body: JSON.stringify({name: newName}) })
            .then(response => {
                if (response.ok) return response.json();
                else throw new Error(response.statusText); })
            .then(result => {
                let index = lists.findIndex(l => l.id === id);
                let newLists = lists.slice();
                newLists[index] = result;
                setLists(newLists);
            });
    }

    function deleteList(id) {
        console.log("Deleting:", api + "/" + currentListId + "/todos/" + id);
        fetch(api + "/lists/" + id, { method: "DELETE" })
            .then(response => {
                if (!response.ok) throw new Error(JSON.stringify(response)); })
            .then(() => {
                let index = lists.findIndex(l => l.id === id);
                let newLists = lists.slice();
                newLists.splice(index, 1);
                if (id === currentListId) {
                    setCurrentListId(undefined);
                }
                setLists(newLists);
            }).catch(error => console.error("Error deleting todo:", error));

    }

    return <header>
        <details>
            <summary>Lists:</summary>
            {lists.map(list => (
                <div className="grid" key={list.id}>
                    <input defaultValue={list.name} onChange={e => {list.name = e.target.value;}}/>
                    <button onClick={() => renameList(list.id, list.name)}>Rename</button>
                    <button className="caution" onClick={() => deleteList(list.id)}>Delete</button>
                    <button onClick={() => setCurrentListId(list.id)}>Select</button>
                </div>
            ))}
            <div className="grid">
                <input value={newListName} onChange={e => {setNewListName(e.target.value)}}/>
                <button onClick={createList}>Create new list</button>
            </div>
        </details>
    </header>;
}