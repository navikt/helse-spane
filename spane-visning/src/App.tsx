import { useEffect, useState } from "react";
import "./App.css";

function App() {
  const [person, setPerson] = useState<any>();
  async function fetchAPI() {
    await fetch("/fnr/10877799145")
      .then((res) => {
        return res.json();
      })
      .then((data) => {
        setPerson(data);
      });
  }

  useEffect(() => {
    fetchAPI();
  }, []);

  return (
    <>
      <h1>no Spane no gain</h1>
      <div>People call me the jarlinator, but you can call me tonight</div>
      <div>
        Liste over vedtaksperioder
        {person
          ? person.vedtaksperioder.map((info: any) => {
              return <div style={{ display: "block" }}>{JSON.stringify(info)}</div>;
            })
          : "fant ingen vedtaksperioder"}
      </div>
    </>
  );
}

export default App;
