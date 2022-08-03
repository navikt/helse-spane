import React from 'react'
import {byggStringRekursivt} from "./utlis";

test('input blir parset rett av byggStringRekursivt', async () => {
    const json = require('./resources/testInput.json');

    console.log(byggStringRekursivt(json))

})