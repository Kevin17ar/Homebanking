window.addEventListener('load', () => {
    const contenedor_loader = document.querySelector('.contenedor')
    contenedor_loader.style.opacity = 0
    contenedor_loader.style.visibility = 'hidden'
})

const app = Vue.createApp({
    data() {
        return {
            cliente: [],
            cuentas: [],
            otherAccount: "",
            amount: "",
            description: "",
            accountOrigin: "",
            accountDestiny: "",
            accountDestinyOther: "",
        }
    },
    created() {
        axios.get("/api/clients/current")
            .then(res => {
                this.cliente = res.data
                this.cuentas = res.data.accounts.filter(cuenta => cuenta.active);

                console.log(this.cliente)
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

        transfer() {
            if (this.accountOrigin != this.accountDestiny) {
                console.log(this.accountOrigin, this.accountDestiny)
                axios.post('/api/clients/current/transactions', "amount=" + this.amount + "&description=" + this.description + "&numberOrigin=" + this.accountOrigin + "&numberDestiny=" + this.accountDestiny, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                    .then(response => {
                        swal({
                                title: "Congratulations",
                                text: "Successful transfer",
                                icon: "success",
                                button: "OK",
                            })
                            .then(res => { location.href = "/accounts.html" })
                    })
                    .catch(err => {
                        swal({
                            title: "Alert",
                            text: "Check the data entered",
                            icon: "warning",
                            button: "OK",
                        })
                    })
            } else {
                swal({
                    title: "Alert",
                    text: "Choose another account",
                    icon: "warning",
                })
            }
        },
        transferOther() {
            if (this.accountOrigin != this.accountDestiny) {
                console.log(this.accountOrigin, this.accountDestiny)
                axios.post('/api/clients/current/transactions', "amount=" + this.amount + "&description=" + this.description + "&numberOrigin=" + this.accountOrigin + "&numberDestiny=" + this.accountDestinyOther, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                    .then(response => {
                        swal({
                                title: "Congratulations",
                                text: "Successful transfer",
                                icon: "success",
                                button: "OK",
                            })
                            .then(res => location.href = "/accounts.html")
                    })
                    .catch(err => {
                        swal({
                            title: "Alert",
                            text: "Check the data entered",
                            icon: "warning",
                        })
                    })
            } else {
                swal({
                    title: "Alert",
                    text: "choose another destination account",
                    icon: "warning",
                })
            }
        }
    },
    computed: {
        showButton() {
            if (this.otherAccount) {
                return true;
            } else {
                return false;
            }
        },
    },
})
app.mount("#app")

function onlyNumber(e) {
    key = e.keyCode || e.which;
    teclado = String.fromCharCode(key);
    numbers = "0123456789";
    especiales = "8-37-38-46";
    teclado_especial = false;

    for (let i in especiales) {
        if (key == especiales[i]) {
            teclado_especial = true;
        }
    }
    if (numbers.indexOf(teclado) == -1 & !teclado_especial) {
        return false;
    }

}