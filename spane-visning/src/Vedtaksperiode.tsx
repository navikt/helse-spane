import React from "react";
import { SubsumsjonDto } from "./App";
import Subsumsjon from "./Subsumsjon";

interface Props {
  subsumsjoner: SubsumsjonDto[];
}

function Vedtaksperiode(props: Props) {
  const { subsumsjoner } = props;

  return (
    <div>
      <h3>Vedtaksperiode</h3>
      Subsumsjoner:
      {subsumsjoner.map((subsumsjon: SubsumsjonDto) => (
        <Subsumsjon
          id={subsumsjon.id}
          kilde={subsumsjon.kilde}
          paragraf={subsumsjon.paragraf}
          lovverk={subsumsjon.lovverk}
        />
      ))}
    </div>
  );
}

export default Vedtaksperiode;
