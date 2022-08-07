import React from "react";
import { PersonDto, VedtaksperiodeDto } from "../../types";
import { Table } from "@navikt/ds-react";
import PseudovedtaksperiodeRad from "./PseudovedtaksperiodeRad";
import { filtrerPseudovedtaksperioder } from "../../utlis";
import "./pseudovedtaksperiodetabell.css";

interface Props {
  valgte: string[];
  fraDato: string;
  tilDato: string;
  person: PersonDto | undefined;
  anonymisert: Boolean;
}

export default function PseudovedtaksperiodeTabell(props: Props) {
  const { valgte, fraDato, tilDato, person, anonymisert } = props;

  return (
    <main className="pseudovedtaksperiodetabell-container">
      <Table size="medium">
        {person && (
          <Table.Body>
            {filtrerPseudovedtaksperioder(person, valgte, fraDato, tilDato).map(
              (vedtaksperiode: VedtaksperiodeDto, vedtaksperiodeIdx) => (
                <PseudovedtaksperiodeRad
                  key={vedtaksperiodeIdx}
                  vedtaksperiode={vedtaksperiode}
                  anonymisert={anonymisert}
                />
              )
            )}
          </Table.Body>
        )}
      </Table>
    </main>
  );
}
