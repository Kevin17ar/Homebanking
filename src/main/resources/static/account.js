window.addEventListener('load', () => {
    const contenedor_loader = document.querySelector('.contenedor')
    contenedor_loader.style.opacity = 0
    contenedor_loader.style.visibility = 'hidden'
})

const app = Vue.createApp({
    data() {
        return {
            cuenta: [],
            transacciones: [],
            cliente: [],
            listFilter: [],
            star: null,
            end: null,
            table: false,
        }
    },
    created() {
        const urlParams = new URLSearchParams(window.location.search);
        const myParam = urlParams.get('id');
        console.log(myParam);

        axios.get("/api/accounts/" + myParam)
            .then(res => {
                this.cuenta = res.data
                this.transacciones = res.data.transactions.sort((a, b) => b.id - a.id)
            })
            .catch(err => console.log(err))

        axios.get("/api/clients/current")
            .then(res => {
                this.cliente = res.data
            })
            .catch(err => console.log(err))
    },
    methods: {
        signOut() {
            axios.post('/api/logout')
                .then(response => {
                    location.href = "/index.html"
                })
        },
        filterDate() {
            const urlParams = new URLSearchParams(window.location.search);
            const myParam = urlParams.get('id');
            console.log(myParam);

            // let star = this.star.split("-").reverse().join("-");
            // let end = this.end.split("-").reverse().join("-");
            console.log(star)
            console.log(end)

            axios.post('/api/clients/filter', "id=" + myParam + "&star=" + this.star + "&end=" + this.end)
                .then(res => {
                    this.listFilter = res.data.sort((a, b) => b.id - a.id)
                    this.table = true
                    console.log(this.listFilter)
                })
                .catch(err => console.log(err))
        },
        resert() {
            location.reload();
        }
    },
    computed: {

    }
})
app.mount("#app")