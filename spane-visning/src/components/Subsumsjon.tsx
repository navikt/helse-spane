import React from "react";

interface Props {
  id: string;
  kilde: string;
  paragraf: string;
  lovverk: string;
}

function Subsumsjon(props: Props) {
  const { id, kilde, paragraf, lovverk } = props;

  return (
    <div>
      §{paragraf}, lovverk: {lovverk}, kilde: {kilde}, id: {id}
    </div>
  );
}

export default Subsumsjon;
