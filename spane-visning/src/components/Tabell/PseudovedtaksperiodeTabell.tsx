import React from "react";
import { PersonDto, VedtaksperiodeDto } from "../../types";
import { Table } from "@navikt/ds-react";
import PseudovedtaksperiodeRad from "./PseudovedtaksperiodeRad";
import { filtrerPseudovedtaksperioder } from "../../utlis";

interface Props {
  valgte: string[];
  fraDato: string;
  tilDato: string;
  person: PersonDto | undefined;
  anonymisert: boolean;
}

export default function PseudovedtaksperiodeTabell(props: Props) {
  const { valgte, fraDato, tilDato, person, anonymisert } = props;

  return (
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
  );
}
