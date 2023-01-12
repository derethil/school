import "./App.css";

function App() {
  return (
    <main className="wrapper">
      <form id="form">
        <label htmlFor="search">
          <h1>Quote Search</h1>
        </label>

        <div id="search-container">
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512" />
          <input type="text" id="search" name="search" />
        </div>
      </form>
    </main>
  );
}

export default App;
