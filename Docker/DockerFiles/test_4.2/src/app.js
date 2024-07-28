const express = require('express');
const app = express();
const port = 3000;
const path = require('path');

app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');

app.get('/', (req, res) => {
  const color = process.env.COLOR || 'green';
  const username = '사용자';
  res.render('index', { color, username });
});

app.get('/:name', (req, res) => {
  const color = process.env.COLOR || 'green';
  const username = req.params.name;
  res.render('index', { color, username });
});

app.listen(port, () => {
  console.log(`Simple Node.js web app is running at http://localhost:${port}`);
});
