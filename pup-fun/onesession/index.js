let Template = (
    url
) => {
    return `
      <!DOCTYPE html>
      <html>
        <head>
           <title>|pipe| session</title>
        </head>
        <body>
           <h1>Running</h1>
           <pre>${url}</pre>
        </body>
      </html>`;
}

 
/**
 * Responds to any HTTP request.
 *
 * @param {!express:Request} req HTTP request context.
 * @param {!express:Response} res HTTP response context.
 */
 
exports.onesession = (req, res) => {
  
const puppeteer = require('puppeteer');
const escapeHtml = require('escape-html');

var wait = ms => new Promise(resolve => setTimeout(resolve, ms));

function run (arg) {
    return new Promise(async (resolve, reject) => {
        try {
            const browser = await puppeteer.launch({args: ['--no-sandbox']});
            const page = await browser.newPage();
            var pipe = "https://dev.pi.pe/borrow.html?dur=h#"+arg;
            await page.goto(pipe);
            console.log('browser in play');
            await wait(60000);               
            browser.close();
            resolve("Done");
        } catch (e) {
            return reject(e);
        }
    })
}
var arg = escapeHtml(req.query.arg || req.body.arg)
console.log("arg = "+arg);
run(arg)
.then(message => {
  console.log("Ran |pipe| " + message);
  res.set('Content-Type', 'text/html');
  res.status(200).send(Template(message));
})
.catch(err => {
    console.error(err);
    res.status(500).send("An Error occured" + err);  
  })
  
};

