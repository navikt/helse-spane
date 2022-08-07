import { SortState, Table } from "@navikt/ds-react";
import { SubsumsjonDto } from "../../types";
import SubsumsjonRad from "./SubsumsjonRad";
import { useState } from "react";

interface Props {
  subsumsjoner: SubsumsjonDto[];
  anonymisert: Boolean;
}

export default function SubsumsjonTabell(props: Props) {
  const { subsumsjoner, anonymisert } = props;
  const [sorterteSubsumsjoner, setSorterteSubsumsjoner] =
    useState(subsumsjoner);
  const [sort, setSort] = useState<SortState>({
    orderBy: "",
    direction: "descending",
  });

  function utfallStringVerdi(str: string) {
    switch (str) {
      case "VILKAR_OPPFYLT":
        return 0;
      case "VILKAR_BEREGNET":
        return 1;
      case "VILKAR_UAVKLART":
        return 2;
      case "VILKAR_IKKE_OPPFYLT":
        return 3;
      default:
        return 4;
    }
  }

  function sorterPåParagraf(
    subsumsjonA: SubsumsjonDto,
    subsumsjonB: SubsumsjonDto
  ): number {
    let kapittel1 = parseInt(subsumsjonA.paragraf.split("-")[0]);
    let kapittel2 = parseInt(subsumsjonB.paragraf.split("-")[0]);

    if (kapittel1 == kapittel2) {
      let paragraf1 = parseInt(subsumsjonA.paragraf.split("-")[1]);
      let paragraf2 = parseInt(subsumsjonB.paragraf.split("-")[1]);

      if (paragraf1 == paragraf2) {
        let ledd1 = subsumsjonA.ledd ?? 0;
        let ledd2 = subsumsjonB.ledd ?? 0;

        if (ledd1 == ledd2) {
          let punktum1 = subsumsjonA.punktum ?? 0;
          let punktum2 = subsumsjonB.punktum ?? 0;

          if (punktum1 == punktum2) {
            let bokstav1 = subsumsjonA.bokstav ?? "";
            let bokstav2 = subsumsjonB.bokstav ?? "";

            if (bokstav1 != null && bokstav2 != null) {
              return bokstav1 > bokstav2 ? 1 : -1;
            } else return 0;
          } else return punktum1 - punktum2;
        } else return ledd1 - ledd2;
      } else return paragraf1 - paragraf2;
    } else return kapittel1 - kapittel2;
  }

  function handleSort(sortKey: string | undefined) {
    if (!sortKey) return;

    if (sort.direction === "ascending") {
      setSort((s) => ({ orderBy: sortKey, direction: "descending" }));
    } else {
      setSort((s) => ({ orderBy: sortKey, direction: "ascending" }));
    }

    if (sortKey === "paragraf") {
      setSorterteSubsumsjoner(
        [...sorterteSubsumsjoner].sort((v1, v2) => {
          return sort.direction === "ascending"
            ? sorterPåParagraf(v2, v1)
            : sorterPåParagraf(v1, v2);
        })
      );
    } else if (sort.orderBy === "utfall") {
      let flip = sort.direction === "ascending" ? -1 : 1;

      setSorterteSubsumsjoner(
        [...sorterteSubsumsjoner].sort((v1, v2) => {
          return utfallStringVerdi(v1.utfall) > utfallStringVerdi(v2.utfall)
            ? flip
            : -flip;
        })
      );
    } else if (sort.orderBy === "behandlet") {
      let flip = sort.direction === "ascending" ? -1 : 1;
      setSorterteSubsumsjoner(
        [...sorterteSubsumsjoner].sort((v1, v2) => {
          return new Date(v1.tidsstempel) > new Date(v2.tidsstempel)
            ? flip
            : -flip;
        })
      );
    }
  }

  return (
    <Table
      size="medium"
      sort={sort}
      onSortChange={(sortKey) => handleSort(sortKey)}
    >
      <Table.Header>
        <Table.Row>
          <Table.ColumnHeader scope="col" sortKey={"paragraf"} sortable>
            Paragraf
          </Table.ColumnHeader>
          <Table.ColumnHeader scope="col" sortKey={"behandlet"} sortable>
            Behandlet
          </Table.ColumnHeader>
          <Table.ColumnHeader scope="col" sortKey={"utfall"} sortable>
            Utfall
          </Table.ColumnHeader>
          <Table.ColumnHeader scope="col"></Table.ColumnHeader>
        </Table.Row>
      </Table.Header>
      <Table.Body>
        {sorterteSubsumsjoner.map((subsumsjon, subsumsjonIdx) => {
          return (
            <SubsumsjonRad
              key={subsumsjonIdx}
              subsumsjon={subsumsjon}
              anonymisert={anonymisert}
            />
          );
        })}
      </Table.Body>
    </Table>
  );
}
