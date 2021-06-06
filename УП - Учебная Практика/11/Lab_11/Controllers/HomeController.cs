using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using Lab_11.Models;

namespace Lab_11.Controllers
{
    public class HomeController : Controller
    {
        private readonly ILogger<HomeController> _logger;
        public HomeController(ILogger<HomeController> logger) => _logger = logger;
        public IActionResult Index() => View();
        public IActionResult Privacy() => View();
        public IActionResult About() => View();

        [HttpPost]
        public IActionResult Area(string text, int size, int count)
        {
            ViewBag.textBox = text.Divider(text.Length / count);
            ViewBag.size = size + "px";
            return View();
        }
        
        [ResponseCache(Duration = 0, Location = ResponseCacheLocation.None, NoStore = true)]
        public IActionResult Error() => View(
            new ErrorViewModel {
            RequestId = Activity.Current?.Id 
                        ?? HttpContext.TraceIdentifier
        });
    }
}